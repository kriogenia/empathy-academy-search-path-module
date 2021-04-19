# Empathy.co - Academy Search Path

Repository developed during the Search Path of the Empathy Academy of February 2021.
This repository uses Micronaut to create a Search API against a docker image of ElasticSearch hosted on the 9200 port.

## API Deployment

Clone the repository and open a new terminal inside the empathy-micronaut-demo folder.
Then use gradle to run the application:

```sh
git clone https://github.com/kriogenia/empathy-micronaut-demo.git
git cd empathy-micronaut-demo
./gradlew run
```

The first time this can take a while as the project installs all the dependencies.
The terminal will tell you that the startup completed, and the server is running on http://localhost:8080 when it's ready.
Open http://localhost:8080/hello on your browser of choice to check everything is ok.
You can also open a new terminal check it with the following command on a new terminal:

```sh
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

If you've got an OK response with the correct version number, you can jump to start indexing IMDB.
You also can, and we suggest you to, saving the docker state to have it as a backup just in case.
To do so run the following command to commit the image with the name you want (version 1 in this case):

```sh
docker commit `docker ps -q` elasticsearch:version1
```

To restore and run that saved image run the following command:

```sh
docker run --rm 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:version1
```

## Indexing IMDB

This API uses two of the IMDB datasets available [here](https://datasets.imdbws.com).
Those are the **title.basics.tsv.gz** and **title.ratings.tsv.gz**. 
Download it and extract its content on [/src/main/resources/imdb](/src/main/resources/imdb) 
with the names **title.basics.tsv** and **title.ratings.tsv**. 
Or just run the following commands.

```sh
cd src/main/resources/imdb
curl -o title.basics.tsv.gz https://datasets.imdbws.com/title.basics.tsv.gz && gzip -d title.basics.tsv.gz
curl -o title.ratings.tsv.gz https://datasets.imdbws.com/title.ratings.tsv.gz && gzip -d title.ratings.tsv.gz
```

Once you have the files on the correct place, use this command, it will build the whole index.

```sh
curl http://localhost:8080/index/imdb
```

(*NOTE, it will a while, maybe even one whole hour. Yeah, I know, sorry. 
So, go make a coffee. You can see the progress in the terminal with the API running.*)

The moment you get an OK response you can start using the search API.

## Usage

### Search

Send all data requests to:

```
http://localhost:8080?search
```

An empty query will retrieve the top 10 items of the datasets.

#### Queries

| Parameter | Required | Format | Default Value | Description |
|---|:---:|---|---|---|
| query | `no` | \<string\> | \<empty\> | Original title of the entries |
| genres | `no` | \<string\>[,\<string\>]* | \<empty\> | Comma separated list of genres to which titles must belong to |
| type | `no` | \<string\>[,\<string\>]* |  \<empty\> | Comma separated list of types to which titles must belong to |
| year | `no` | \<YYYY\>/\<YYYY\>[,\<YYYY\>/\<YYYY\>]* |  \<empty\> | Comma separated list of ranges, such as 2000/2010, to which titles have been released during such period

For example, the following query searches the action or adventure movies titled Avengers released between 2010 and 2016.

```
http://localhost:8080/search?query=Avengers&genres=Action,Adventure&type=movie&year=2010/2016
```

### Detail

To get the complete details of an entry perform the following request with the id of the entry:

```
http://localhost:8080/titles/{id}
```

### Interface

Each search query will return a JSON, it contains the following fields:

* `total` Total number of hit entries
* *\<optional\>* `items` List of retrieved entries (max. 10).
* *\<optional\>* `aggregations` Different aggregations and the number of hits on each one. Omitting buckets with 0 results.
    * `types` Total hits on each type
    * `year` Total hits of the start_year on each decade
    * `genres` Total hits on each genre
* *\<optional\>* `suggestions` List of terms suggested as alternatives for the terms query
  
#### Item

The items recovered in the searches and detail request will be like this:

  * `id` (*string*) Alphanumeric unique identifier of the title
  * `title` (*string*) The more popular title / the title used by the filmmakers on promotional materials at the time of release
  * *\<optional\>* `genres` (*string array*) Up to three genres associated with the title (see Enumerations - Genres)
  * `type` (*string*) The type/format of the title (see Enumerations - Types)
  * `start_year` (*YYYY*) Represents the release year of a title. 
    In the case of TV Series, it is the series start year.
  * *\<optional\>* `end_year` (*YYYY*) TV Series end year 
  * *\<optional\>* `average_rating` (*double*) weighted average of all the individual user ratings
  * *\<optional\>* `num_votes` (*integer*) number of votes the title has received

The details request will also feature the following fields:

  * `original_title` (*string*) original title, in the original language
  * `is_adult` (*boolean*) if it's an adult title
  * *\<optional\>* `runtime_minutes` (*string*) primary runtime of the title, in minutes

#### Enumerations

* **Genres** (27): Action, Adventure, Animation, Biography, Comedy, Crime, Documentary, Drama, Family, Fantasy, Film Noir, 
  Game Show, History, Horror, Music, Musical, Mystery, News, Reality-TV, Romance, Sci-Fi, Sport, Talk Show, Thriller, 
  War, Western
  
* **Types** (11): movie, tvMovie, tvSeries, tvEpisode, tvSpecial, miniSeries, documentary, videoGame, short, video, tvShort

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
