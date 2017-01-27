(ns mars-rover.rover
  (:require [mars-rover.worlds :refer [rover-not-hitting-obstacle wrap]]))

(defn rover [x y direction]
  {:x x :y y :direction direction})

(defn- rover-in-valid-position [world current-rover x y direction]
  (rover-not-hitting-obstacle
    current-rover
    (wrap world (rover x y direction))
    world))

(defn process [commands {direction :direction :as initial-rover} world]
  (let [rover-in-valid-position (partial rover-in-valid-position world)]

    (letfn
      [(facing-north
         [{:keys [x y] :as current-rover} [command & rest-commands]]
         #(if command
           (case command
             :rotate-right
             (facing-east (rover x y :east) rest-commands)

             :rotate-left
             (facing-west (rover x y :west) rest-commands)

             :move-forwards
             (facing-north
               (rover-in-valid-position current-rover x (inc y) :north)
               rest-commands)

             :move-backwards
             (facing-north
               (rover-in-valid-position current-rover x (dec y) :north)
               rest-commands))
           current-rover))

       (facing-south
         [{:keys [x y] :as current-rover} [command & rest-commands]]
         #(if command
           (case command
             :rotate-right
             (facing-west (rover x y :west) rest-commands)

             :rotate-left
             (facing-east (rover x y :east) rest-commands)

             :move-forwards
             (facing-south
               (rover-in-valid-position current-rover x (dec y) :south)
               rest-commands)

             :move-backwards
             (facing-south
               (rover-in-valid-position current-rover x (inc y) :south)
               rest-commands))
           current-rover))

       (facing-east
         [{:keys [x y] :as current-rover} [command & rest-commands]]
         #(if command
           (case command
             :rotate-right
             (facing-south (rover x y :south) rest-commands)

             :rotate-left
             (facing-north (rover x y :north) rest-commands)

             :move-forwards
             (facing-east
               (rover-in-valid-position current-rover (inc x) y :east)
               rest-commands)

             :move-backwards
             (facing-east
               (rover-in-valid-position current-rover (dec x) y :east)
               rest-commands))
           current-rover))

       (facing-west
         [{:keys [x y] :as current-rover} [command & rest-commands]]
         #(if command
           (case command
             :rotate-right
             (facing-north (rover x y :north) rest-commands)

             :rotate-left
             (facing-south (rover x y :south) rest-commands)

             :move-forwards
             (facing-west
               (rover-in-valid-position current-rover (dec x) y :west)
               rest-commands)

             :move-backwards
             (facing-west
               (rover-in-valid-position current-rover (inc x) y :west)
               rest-commands))
           current-rover))

       (initial-state-fn
         [direction]
         (case direction
           :north facing-north
           :south facing-south
           :east facing-east
           :west facing-west))]

      (trampoline (initial-state-fn direction)
                  initial-rover
                  commands))))