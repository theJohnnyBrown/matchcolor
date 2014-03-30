(ns matchcolor.server
  (:require [matchcolor.views :as views]
            [matchcolor.routes :as routes]
            [secretary.core :as secretary]))

(def __dirname (if (exists? (js* "__dirname")) (js* "__dirname")))
; Define namespaced references to Node's externed globals:
(def node-require (if __dirname (js* "require")))
(def node-process (if __dirname (js* "process")))

(defn -main [& args]
  (let [express (node-require "express")
        logfmt (node-require "logfmt")
        app (express)
        port (or (aget node-process "env" "PORT") 3000)]
    (.log js/console __dirname)
   (doto app

     ; Logger
     (.use (.requestLogger logfmt))

     ; Body parser
     (.use (.urlencoded express))
     (.use (.json express))

     ; Set assets folder
     (.use (.static express (str __dirname "/../assets")))

     ; Redirect form to to /MYHEXCOLOR
     (.post "/" #(.redirect %2 (str "/" (aget %1 "body" "color"))))

     (.get "*"
           (fn [req resp next]
             (let [{:keys [template active] :as view}
                   (secretary/dispatch! (.-url req))]
               (if view (.send resp (views/layout-render template active)) (next)))))

     (.get "*" #(.send %2 (views/layout-render views/four-oh-four "404") 404))


     (.listen port))))
