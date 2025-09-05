/**
 * Kaalka FileWeave: Chunked file encryption/decryption and manifest
 * SECURITY WARNING: Kaalka is a custom, time-based cipher. This wrapper uses Kaalka Drum calls only and is experimental. Do NOT use in production without a formal cryptographic audit.
 */

const fs = require('fs').promises;
const path = require('path');
const { weaveFrom, drumEncrypt, drumDecrypt } = require('./kaalka_time_protocol');

const CHUNK_SIZE = 1024 * 1024; // 1MB

async function encryptFile(inputPath, resonance, beatTimeIso, manifestPath) {
  const stat = await fs.stat(inputPath);
  const totalChunks = Math.ceil(stat.size / CHUNK_SIZE);
  const manifest = { chunks: [], size: stat.size, inputFile: inputPath };
  const fd = await fs.open(inputPath, 'r');
  for (let i = 0; i < totalChunks; i++) {
    // Use protocol-correct beat time for each chunk
    const chunkBeat = require('./kaalka_time_protocol').addSeconds(beatTimeIso, i);
    const { loom, nonceSeed, sealSeed } = require('./kaalka_time_protocol').weaveFrom(resonance, chunkBeat);
    const chunkKey = require('./kaalka_time_protocol').drumEncrypt(
      Buffer.concat([nonceSeed, require('./kaalka_time_protocol').bigEndian(i, 4)]),
      nonceSeed,
      chunkBeat
    );
    const buf = Buffer.alloc(Math.min(CHUNK_SIZE, stat.size - i * CHUNK_SIZE));
    await fd.read(buf, 0, buf.length, i * CHUNK_SIZE);
    const ct = require('./kaalka_time_protocol').drumEncrypt(buf, chunkKey, chunkBeat);
    manifest.chunks.push({ index: i, size: buf.length, beat: chunkBeat });
    const chunkPath = path.resolve(path.dirname(inputPath), `${path.basename(inputPath)}.chunk${i}`);
    await fs.writeFile(chunkPath, ct);
  }
  await fd.close();
  await fs.writeFile(manifestPath, JSON.stringify(manifest, null, 2));
}

async function decryptFile(outputPath, resonance, beatTimeIso, manifestPath) {
  const manifest = JSON.parse(await fs.readFile(manifestPath, 'utf8'));
  const outPath = path.resolve(process.cwd(), outputPath);
  console.log('[FileWeave] Decrypting to:', outPath);
  const fd = await fs.open(outPath, 'w');
  const inputBase = manifest.inputFile;
  try {
    for (const chunk of manifest.chunks) {
      // Use protocol-correct beat time for each chunk
      const chunkBeat = chunk.beat;
      const { loom, nonceSeed, sealSeed } = require('./kaalka_time_protocol').weaveFrom(resonance, chunkBeat);
      const chunkKey = require('./kaalka_time_protocol').drumEncrypt(
        Buffer.concat([nonceSeed, require('./kaalka_time_protocol').bigEndian(chunk.index, 4)]),
        nonceSeed,
        chunkBeat
      );
      const chunkPath = path.resolve(path.dirname(inputBase), `${path.basename(inputBase)}.chunk${chunk.index}`);
      const ct = await fs.readFile(chunkPath);
      const pt = require('./kaalka_time_protocol').drumDecrypt(ct, chunkKey, chunkBeat);
      // Write only the actual chunk size
      await fd.write(pt, 0, chunk.size, chunk.index * CHUNK_SIZE);
    }
  } catch (err) {
    console.error('[FileWeave] Error during decryption:', err);
    throw err;
  } finally {
    await fd.close();
    console.log('[FileWeave] Closed file handle for:', outPath);
  }
}

module.exports = { encryptFile, decryptFile };
