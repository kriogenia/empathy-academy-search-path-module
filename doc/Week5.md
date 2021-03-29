#Week 5

##2021-03-22
Today I made a shy approach to the ImdbSearcher tests mocking the SearchEngine.
I didn't work as I intended but making some changes I was able to make it work.

Our path meeting today won't happen so instead of a new extension we had to make some deployment tutorial.
The front-end folks will use our APIs to their front pages, so they should be able to easily use them.

##2021-03-23
My indexing broke, and I still don't know why, some bean problem with the client of ElasticSearch.
So I decided to finally move the indexing to a controller, so it's easier to use.

We had our weekly meeting and strong part is coming, we are starting with the scores.
That means thinking about all the fields, how to evaluate them, adding a new index...
This week looks tough but pretty funny.

##2021-03-24
First things first, will need to add the ratings to our current index.
I was searching a couple of hours how to handle this. It looks like bulk updating is the way to go.
So, finally I created a new common object, the ImdbRatings to manage the ratings with a different object.

Next, I added an indexer function to those updates, extracting some functional logic in the process.
I had to change some things in the index configurations. 
To be fair, I don't like those changes, so it's possible that I change that approach.
This was harder than I expected.

##2021-03-25
Objective of the day: Design queries and functions, test them, measure them and select a winner.
I'll need a reindexing to add the perfect match boost, and I'll add both titles also.

That was my whole morning, refining queries and results.
I think I have a candidate to be the definitive query.
That means I can jump back to Java to break my head thinking about how to implement all this mess.

##2021-03-26
So I wake up today, and the first thing in the morning I saw was a petition to make a presentation today in front of everyone.
At first, we didn't like the idea but half and hour before the meeting we decided to jump and make it.
I made some queries to show my improvements this week at max speed, even leading with changing PCs.
And we didn't show our demo at the end, it was a fun ride tho. 
Will show our progress soon.

Jumping from that I was able to make the java conversion of the functions but I'm still lacking the query part.