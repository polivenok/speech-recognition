import edu.cmu.sphinx.frontend.util.Microphone
import edu.cmu.sphinx.recognizer.Recognizer
import edu.cmu.sphinx.result.Result
import edu.cmu.sphinx.util.props.ConfigurationManager

object HelloWorld {

  def main(args: Array[String]) {
    val cm:ConfigurationManager =  new ConfigurationManager(this.getClass.getResource("test.config.xml"))

    val recognizer =  cm.lookup("recognizer").asInstanceOf[Recognizer]
    recognizer.allocate();

    // start the microphone or exit if the programm if this is not possible
    val microphone =  cm.lookup("microphone").asInstanceOf[Microphone]
    if (!microphone.startRecording()) {
      println("Cannot start microphone.")
      recognizer.deallocate()
      sys.exit(1)
    }

    println("Say: (Good morning | Hello) ( Bhiksha | Evandro | Paul | Philip | Rita | Will )")

    // loop the recognition until the programm exits.
    while (true) {
      println("Start speaking. Press Ctrl-C to quit.\n")

      val result:Result = recognizer.recognize()

      if (result != null) {
        val resultText = result.getBestFinalResultNoFiller()
        println("You said: " + resultText + '\n')
      } else {
        println("I can't hear what you said.\n")
      }
    }
  }
}
