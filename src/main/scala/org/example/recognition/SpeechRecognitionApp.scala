package org.example.recognition

import java.net.URL

import edu.cmu.sphinx.api._
import scala.collection.JavaConversions._

object SpeechRecognitionApp extends App {

  val configuration = new Configuration
  // Set path to acoustic model.
  configuration.setAcousticModelPath("file:models/acoustic/wsj")
  // Set path to dictionary.
  configuration.setDictionaryPath("file:models/acoustic/wsj/dict/cmudict.0.6d")
  // Set language model.
  configuration.setLanguageModelPath("models/language/en-us.lm.dmp")

  def testMic() {

    // loop the recognition until the programm exits.
    while (true) {
      println("Start speaking. Press Ctrl-C to quit.\n")

      val recognizer = new LiveSpeechRecognizer(configuration)
      // Start recognition process pruning previously cached data.
      recognizer.startRecognition(true)
      var result = recognizer.getResult

      while ({result = recognizer.getResult; result != null}) {
        println(result.getHypothesis)
      }
    }
  }

  def testStream() {
   val aligner = new SpeechAligner(configuration)
    for (r <- aligner.align(new URL("file:src/main/resources/10001-90210-01803.wav"),
      "one zero zero zero one nine oh two one oh zero one eight zero three")) println(r)

    val recognizer = new StreamSpeechRecognizer(configuration)

    recognizer.startRecognition(this.getClass.getResourceAsStream("/10001-90210-01803.wav"))
    var result:SpeechResult = null

    while ({result = recognizer.getResult; result != null}) {
      println(s"Hypothesis:  ${result.getHypothesis} ")

      println("List of recognized words and their times:")
      for (r <- result.getWords) println(r)

      println("Best 3 hypothesis:")
      for (s <- result.getNbest(3)) println(s)

      println(s"Lattice contains ${result.getLattice.getNodes.size}  nodes")
    }
    recognizer.stopRecognition()
  }

  testStream()

}





