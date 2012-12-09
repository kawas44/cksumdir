(ns cksumdir
  (:import [java.io InputStream]
           [java.util Arrays]
           [java.util.zip CRC32])
  (:use [clojure.java.io :only [input-stream as-file]]))


(set! *warn-on-reflection* true)


(defn abyte-seq [^InputStream st ^Integer len]
  "Read input stream as a lazy sequence of non empty byte arrays"
  (let [step (fn step [^bytes buf]
               (lazy-seq
                 (let [n (.read st buf)]
                   (cond
                     (pos? n) (cons (Arrays/copyOf buf n) (step buf))
                     (zero? n) (step buf)))))]
    (step (byte-array len))))

(defn crc32 [input]
  "Compute CRC32 from the specified ressource coerse as an InputStream"
  (with-open [stream (input-stream input)]
    (let [crc (CRC32.)]
      (doseq [^bytes ab (abyte-seq stream 8192)]
        (.update crc ab))
      (.getValue crc))))

; MAIN
(let [files (map as-file *command-line-args*)]
  (doseq [f (filter #(.isFile %1)
              (mapcat file-seq files))]
    (println (str f) (crc32 f))))
