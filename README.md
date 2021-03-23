# Empathy.co - Academy Search Path

Repository developed during the Search Path of the Empathy Academy of February 2021.
This repository uses Micronaut to create a Search API against a docker image of ElasticSearch hosted on the 9200 port.

## API Deployment

Clone the repository and open a new terminal inside the empathy-micronaut-demo folder. Enter the following command:

```shell
.\gradlew run
```

The first time this can take a while as the project installs all the dependencies.
The terminal will tell you that the startup completed, and the server is running on http://localhost:8080 when it's ready.
Use the following command on a new terminal or open http://localhost:8080/hello on your browser of choice to check everything is ok.

```shell
curl -s http://localhost:8080/hello
```

If you got the Hello World response then jump to the next section.

## ElasticSearch Deployment

We'll run our indices on a ElasticSearch docker.
You can download Docker [here](https://www.docker.com/products/docker-desktop).
Once it is installed, open a terminal and run the following commands 

```sh
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

Once you have the file on its place just use this command ,and it will build the whole index.
(*NOTE, it will a while, several minutes at least. So, go make a coffee*)

```sh
curl http://localhost:8080/index/imdb
```

The moment you get an OK response you can start using the search API.

## Usage

Send all data requests to:

```
http://localhost:8080?search
```

### Queries

| Parameter | Required | Format | Default Value | Description |
|---|:---:|---|---|---|
| query | `yes` | \<string\> | | Original title of the entries |
| genres | `no` | \<string\>[,\<string\>]* | \<empty\> | Comma separated list of genres to which titles must belong to |
| type | `no` | \<string\>[,\<string\>]* |  \<empty\> | Comma separated list of types to which titles must belong to |
| year | `no` | \<YYYY\>/\<YYYY\>[,\<YYYY\>/\<YYYY\>]* |  \<empty\> | Comma separated list of ranges, such as 2000/2010, to which titles have been released during such period

For example, the following query searches the action or adventure movies titled Avengers released between 2010 and 2016.

```
http://localhost:8080/search?query=Avengers&genres=Action,Adventure&type=movie&year=2010/2016
```


### Interface

Each API query will return a JSON, it will the following fields:

* `total` Total number of hit entries
* `items` List of retrieved entries (max. 10). Each of those items have the following fields:
    * `id` (*string*) Alphanumeric unique identifier of the title
    * `title` (*string*) The more popular title / the title used by the filmmakers on promotional materials at the time of release
    * `genres` (*string array*) Up to three genres associated with the title (see Enumerations - Genres)
    * `type` (*string*) The type/format of the title (see Enumerations - Types)
    * `start_year` (*YYYY*) Represents the release year of a title. 
      In the case of TV Series, it is the series start year.
    * *\<optional\>* `end_year` (*YYYY*) TV Series end year 
* `aggregations` Different aggregations and the number of hits on each one. Omitting buckets with 0 results.
    * `types` Total hits on each type
    * `year` Total hits of the start_year on each decade
    * `genres` Total hits on each genre

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
