Gemini API Key setup
--------------------

This project uses Google Gemini (Generative Language API). Do NOT store your API key in source control.

Recommended steps:

- Set the `GEMINI_API_KEY` environment variable on your host or CI environment.

- Example (Linux/macOS):

```bash
export GEMINI_API_KEY="your-real-key-here"
```

- When running with Docker Compose, provide the key via environment or a `.env` file (do not commit the `.env` file).

- The Spring Boot configuration reads `ai.api-key` from environment variable `GEMINI_API_KEY`.

If you accidentally committed a key, purge it from git history and rotate the key immediately.
