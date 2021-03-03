#Week 2

## 01/03/2021

Today we started the second week.
Our new goal is to load the IMDb database into ElasticSearch and make searches from our application.
The rest of the day I was able to read the file (damn, it's big) and call to ES for the indexing.

It. Was. Slow. It would take me like 35h to process the whole document.
So I did stop the processing and tomorrow I will make the bulk indexing that we were projected to implement too.

## 02/03/2021

Implement the bulk indexing didn't take long thanks to the index effort in the previous day.
Now the whole index takes around and hour to finish, but it crashed me.
I will make a couple of log changes to that process and try again out of the working time.

My next goal will be to make the search api call to retrieve those options matching the title.
The most difficult part was creating an object to use as interface for all the results.
Whatever, it works.

Tomorrow I will make some improvements, and I have to take care of some TODOs I did let.
If I have enough time, I should also develop the tests.

## 03/03/2021

Today my main goal is to build the search function properly with what I think is the best possible approach.
The first problem was dealing with the generics, and the SearchResponse item that uses them, but I think I got it abstracting from that class.

The next point is adapting the output to the required names and order.
As I will do this changing how I indexed the data I will leave it with the old names for now.
At the moment I index all again I'll make the change.