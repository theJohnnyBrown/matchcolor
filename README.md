# matchcolor

A server-side Node.js app written in ClojureScript to find the match or the closest match to a hexcolor.

This is a fork of Sean Brewer's matchcolor, altered to use om, and render pages on both server and client.

## Why?

This was mainly an experiment in developing a simple web application in ClojureScript, served from Node.js, with templates and routes shared between server and browser.

It represents an attempt to articulate my personal vision of the future of web development, as enabled by react and om. Primarily I wanted to demonstrate the possibility defining a mapping from [GET /url/thatreturns/html/] -> [html page] and re-using it everywhere. You'll get exactly the same view whether you type "matchcolor.com/about" into your browser and send an HTTP request, or click the link on the site and cause the page to re-render.

It uses [express](https://github.com/visionmedia/express), [om](https://github.com/swannodette/om/) for views, [logfmt](https://github.com/kr/logfmt) for logging, and [garden](https://github.com/noprompt/garden) for the hex<->rgb conversions it provides.

The massive color lists comes from [Wikipedia](http://en.wikipedia.org/wiki/Lists_of_colors).

Layout is one of the [Bootstrap examples](http://getbootstrap.com/getting-started/#examples).

## Building and Running

1. `lein deps`
2. `lein cljsbuild once`
4. `cd target && supervisor -w cljsbuild-main.js cljsbuild-main.js`

The app will be accessible on port 3000.

# Deployment

It's already setup for deployment to heroku. Move the compiled `cljsbuild-main.js` to `deploy` and then push to heroku.


## License

Copyright © 2014 Sean Brewer

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
