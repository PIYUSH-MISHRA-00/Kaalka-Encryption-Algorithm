/**
 * Minimal CLI for Kaalka Time-First Protocol
 * SECURITY WARNING: Kaalka is a custom, time-based cipher. This wrapper uses Kaalka Drum calls only and is experimental. Do NOT use in production without a formal cryptographic audit.
 */

const fs = require('fs').promises;
const path = require('path');
const {
  generateSundial,
  encryptEnvelope,
  decryptEnvelope
} = require('../lib/kaalka_time_protocol');

async function main() {
  const args = process.argv.slice(2);
  const cmd = args[0];

  if (cmd === 'gen-sundial') {
    const { sundialSecret, sundialBeaconB64 } = generateSundial();
    await fs.writeFile('sundial.json', JSON.stringify({ sundialSecret: sundialSecret.toString('base64'), sundialBeaconB64 }, null, 2));
    console.log('Sundial generated: sundial.json');
  } else if (cmd === 'encrypt-msg') {
    const recipient = args[args.indexOf('--recipient') + 1];
    const sundialPath = args[args.indexOf('--sundial') + 1];
    const inPath = args[args.indexOf('--in') + 1];
    const outPath = args[args.indexOf('--out') + 1];
    const aad = args.includes('--aad') ? args[args.indexOf('--aad') + 1] : '';
    const seq = args.includes('--seq') ? parseInt(args[args.indexOf('--seq') + 1]) : 1;
    const senderSundial = JSON.parse(await fs.readFile(sundialPath, 'utf8'));
    const plaintext = await fs.readFile(inPath, 'utf8');
    const envelope = encryptEnvelope(plaintext, recipient, Buffer.from(senderSundial.sundialSecret, 'base64'), aad, seq);
    await fs.writeFile(outPath, JSON.stringify(envelope, null, 2));
    console.log('Envelope written:', outPath);
  } else if (cmd === 'decrypt-msg') {
    const envelopePath = args[args.indexOf('--envelope') + 1];
    const sundialPath = args[args.indexOf('--sundial') + 1];
    const outPath = args[args.indexOf('--out') + 1];
    const envelope = JSON.parse(await fs.readFile(envelopePath, 'utf8'));
    const recipientSundial = JSON.parse(await fs.readFile(sundialPath, 'utf8'));
    const plaintext = decryptEnvelope(envelope, Buffer.from(recipientSundial.sundialSecret, 'base64'));
    await fs.writeFile(outPath, plaintext);
    console.log('Decrypted message written:', outPath);
  } else {
    console.log('Usage:');
    console.log('  node cli/kaalka_time_cli.js gen-sundial');
    console.log('  node cli/kaalka_time_cli.js encrypt-msg --recipient <b64> --sundial ./sundial.json --in plain.txt --out env.json');
    console.log('  node cli/kaalka_time_cli.js decrypt-msg --envelope env.json --sundial ./sundial.json --out plain.txt');
  }
}

main().catch(e => { console.error(e); process.exit(1); });
