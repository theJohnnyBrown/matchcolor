(defproject matchcolor "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-npm "0.3.2"]
            [lein-cljsbuild "1.0.2"]]

  :node-dependencies [[react "0.9.0"]
                      [express "3.4.8"]
                      [logfmt "0.20.0"]]

  :cljsbuild
    {:builds
     [{:id "node"
       :source-paths ["src"]
       :compiler
       {:pretty-print true
        :preamble ["matchcolor/react_preamble.js"]
        :target :nodejs
        :optimizations :simple
        :language-in :ecmascript5}}
      {:id "browser"
       :source-paths ["src"]
       :compiler
       {:pretty-print true
        :output-to "assets/js/matchcolor.js"
        :preamble ["react/react.js"]
        :optimizations :simple
        :language-in :ecmascript5}}]}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2197"]
                 [garden "1.1.5"]
                 [om "0.5.2"]
                 [sablono "0.2.15"]
                 [secretary "1.1.0"]])
