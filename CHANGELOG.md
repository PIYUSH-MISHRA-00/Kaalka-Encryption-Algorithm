# Changelog

[5.0.0] - 2025-09-05

    Application-ready time protocol: envelope, seal, replay protection, file chunking, public API.
    Pure Kaalka time-based logic, no external crypto.
    Envelope format: senderId, receiverId, timestamp, window, seq, ciphertext, seal.
    Time-based integrity (seal/MIC) and replay protection.
    File chunking for large file encryption/decryption.
    Flexible API: system UTC or user timestamp.
    Unit tests for envelope, replay, chunking, and time logic.
    Backward compatible with previous Kaalka features.

[4.0.0] - 2025-07-22
Changed

    File/media encryption now uses integer arithmetic for lossless, reversible results (matches Python/JavaScript logic)
    Robust extension handling and cross-language compatibility

Fixed

    All file/media types (images, binary, video, music, text, CSV, JSON, XML, etc.) are now encrypted/decrypted losslessly

Documentation

    README updated to reflect robust, lossless file/media support and cross-language compatibility

[3.0.0] - 2025-07-22
Added

    Media/file encryption and decryption (supports any file type: text, binary, images, etc.)
    Default time as system time, with explicit time override
    NTP time support and packet wrapper
    Robust error handling and input validation
    Thorough test suite for text, binary, UTF-8, large, and media files
    Improved documentation and usage examples

Changed

    API fully matches Python and JavaScript versions
    Extension handling for encrypted/decrypted files
    All code errors and warnings removed

Fixed

    Deprecated patterns and issues resolved
    All tests pass with zero errors

[2.0.0] - 2025-06-29
Added

    Robust, timestamp-based encryption using angles and trigonometric functions
    Full API parity with Python and Node.js Kaalka implementations
    Flexible timestamp support (system, NTP, or custom)
    Packet wrapper for secure message packets
    Comprehensive documentation and usage examples

Changed

    Breaking: All encryption/decryption logic now uses robust timestamp-based approach
    Refactored all APIs for clarity and cross-platform compatibility

Fixed

    Test and example files updated for new API

[1.0.0] - Initial release

    Basic Kaalka encryption/decryption
