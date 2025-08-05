A simple Spring Boot practice project of a URL shortener.

### Features

- URL Shortening: Converts a long URL into a short, unique key. If a URL has already been shortened, the existing key is returned instead of creating a new one.

- Redirection: Redirects the short key to its original URL.

- Link Expiration: Short links expire after a certain number of hours.

- Click Tracking: Counts how many times a short link is used.

### API Endpoints

#### Shorten a URL

- Endpoint: `POST /api/shorten`
- Body:
  ```json
  {
    "originalUrl": "https://very.long/url/here"
  }
  ```
- Response:
  ```json
  {
    "shortUrl": "aB1cDe2"
  }
  ```

#### Redirect

- Endpoint: `GET /{shorUrl}`
- Example: `GET /aB1cDe2`

#### See URL statistics

- Endpoint: `GET /api/stats/{shorUrl}`
- Example: `GET /api/stats/aB1cDe2`
- Response:
  ```json
  {
    "shortUrl": "aB1cDe2",
    "expiresAt": "2025-08-04T00:00:01.123456",
    "clickCount": 15
  }
  ```
