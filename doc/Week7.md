#Week 7

##2021-04-05
My first day at the office and it's a day I don't have much to do at first.
We'll have our usual monday meetings and there's also a responsive web seminar I'll be attending.
Until then, I think I'll be giving a look to my code searching for improvements.

Three meeting later I started looking into our new assignments.
I've half of them already done so first all be returning the whole number of buckets on the aggregations

##2021-04-06
We agreed on how to handle the missing genres, we'll not even return them on the aggregation. 
So I did jump on the definitive change to the genres omitting the \N on the index.

Finished that I went to the extra task of this week (as I already had all the others ready).
It was kinda difficult as it involved some problematic nesting.
We have to make filtered and unfiltered aggregations based on the filters.

The filter change was easy, just moving them to the last point, that made the aggregations return based on the whole query.
Then I had to add filters to the aggregations, this was tricky at first, and I had to think carefully how to handle it.

Thank god, my approach was enough, and I was able to make that filtered aggregation, but that needed something more.
My biggest nightmare in this API was working with the retrieved maps so having to deal with nested results looked bad.
I was able to handle it nicely tho. As of now is not scalable, but it really works.

And thank god I have tests. 
I overlooked my filters, they are not working properly and must be fixed.

##2021-04-07
With all the weekly tasks done I'll jump on the new endpoint.
It should be fairly easy, but I want to make it clean using Jackson and that could be a problem.
I also have a medical appointment that will take half of my morning.
I will recover at afternoon tho.

Thanks to partner advice I was able to think of a good way to handle the deserialization of the item.
So, I was able to make the new endpoint and started to improve the error handling.

Again, thank god I'm making test as I found an error through them that drove me crazy.
It's now fixed tho.

##2021-04-08
Today I decided to improve some little things before we attend to a search team sprint planning.

##2021-04-09
Not a lot to do today aside of the meetings.
So I dedicated to morning to give a look to the ElasticSearch source code.
