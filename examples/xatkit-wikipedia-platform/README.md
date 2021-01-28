Xatkit Wikipedia Platform
=====
Integrate articles from [Wikipedia](https://wikipedia.com/) in your execution model.


## Providers

The Wikipedia platform does not define any provider.

## Actions

| Action | Parameters                                                   | Return                         | Return Type | Description                                                 |
| ------ | ------------------------------------------------------------ | ------------------------------ | ----------- | ----------------------------------------------------------- |
| GetInfo | - `search`(**String**): the search query used to retrieve Wikipedia articles (can be a single word or a sentence) | The `url` of the retrieved article | String      | Returns the first article matching the provided `search` query. |
