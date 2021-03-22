# Empathy.co - Academy Search Path

Repository developed during the Search Path of the Empathy Academy of February 2021.
This repository uses Micronaut to create a Search API against a docker image of ElasticSearch hosted on the 9200 port.
Documentation of interest:

## API Deployment

Clone the repository and open a new terminal inside the empathy-micronaut-demo folder. Enter the following command:

```shell
.\gradlew run
```

The first time this can take a while as the project installs all the dependencies.
The terminal will tell you that the startup completed, and the server is running on http://localhost:8080 when it's ready.
Use the following command on a new terminal or open http://localhost:8080 on your browser of choice to check everything is ok.

```shell
curl -s http://localhost:8080
```

If you got the Hello World response then jump to the next section.
You don't need the API running for it, close it via Ctrl/Command + C.

## ElasticSearch deployment

We'll run our indices on a ElasticSearch docker.
You can download Docker [here](https://www.docker.com/products/docker-desktop).
Once it is installed, open a terminal and run the following commands 

```shell
docker pull docker.elastic.co/elasticsearch/elasticsearch:7.11.1
docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.11.1
curl -XGET http://localhost:9200
```

If you've got an OK response with the correct version number, you can start indexing IMDB.

## Indexing IMDB

This API uses one of the IMDB datasets available [here](https://datasets.imdbws.com).
It's the **title.basics.tsv.gz**. 
Download it and extract its content on [/src/main/java/resources/imdb](/src/main/java/resources/imdb) 
with the name **title.basics.tsv**. 

Now let's move to [/src/main/java/co/empathy/Application.java](/src/main/java/co/empathy/Application.java)
and uncomment the *indexImdb()* call on the main method.
This way, running the app will index the whole dataset on our ElasticSearch container.
Once it's done, the API will be ready to use.

*(With the whole dataset indexed I recommend you to comment that line again.
Doing this, it won't index everything again each time you launch the API saving you a lot of time.)*

## Usage

Send all data requests to:

```
http://localhost:8080?search
```

### Parameters

| Parameter | Required | Format | Default Value | Description |
|---|:---:|---|---|---|
| query | `yes` | \<string\> | | Original title of the entries |
| genres | `no` | \<string\>[,\<string\>]* | \<empty\> | Comma separated list of genres to which titles must belong to |
| type | `no` | \<string\>[,\<string\>]* |  \<empty\> | Comma separated list of types to which titles must belong to |
| year | `no` | \<YYYY\>/\<YYYY\>[,\<YYYY\>/\<YYYY\>]* |  \<empty\> | Comma separated list of ranges, such as 2000/2010, to which titles have been released during such period

### Interface

Each API query will return a JSON, it will the following field:

* `total` total number of hit entries
* `items` list of retrieved entries (max. 10). Each of those items have the following fields:
    * `id` (string) alphanumeric unique identifier of the title
    * `title` (string) the more popular title / the title used by the filmmakers on promotional materials at the time of release
    * `genres` (string array) up to three genres associated with the title (see Enumerations - Genres)
    * `type` (string) the type/format of the title (see Enumerations - Types)
    * `start_year` (YYYY) represents the release year of a title. 
      In the case of TV Series, it is the series start year.
    * `end_year` (YYYY) (Optional) TV Series end year 
* `aggregations` different aggregations and the number of hits on each one. Omitting buckets with 0 results.
    * `types` total hits on each type
    * `year` total hits of the start_year on each decade
    * `genres` total hits on each genre

#### Enumerations

* **Genres**: Action, Adventure, Animation, Biography, Comedy, Crime, Documentary, Drama, Family, Fantasy, Film Noir, 
  Game Show, History, Horror, Music, Musical, Mystery, News, Reality-TV, Romance, Sci-Fi, Sport, Talk Show, Thriller, 
  War, Western
  
* **Types**: movie, tvMovie, tvSeries, tvEpisode, tvSpecial, miniSeries, documentary, videoGame, short, video, tvShort

## Documentation of interest

### Micronaut 2.3.2 Documentation

- [User Guide](https://docs.micronaut.io/2.3.2/guide/index.html)
- [API Reference](https://docs.micronaut.io/2.3.2/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/2.3.2/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

### Feature elasticsearch documentation

- [Micronaut Elasticsearch Driver documentation](https://micronaut-projects.github.io/micronaut-elasticsearch/latest/guide/index.html)

### Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)
