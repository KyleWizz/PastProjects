import khoury.EnabledTest
import khoury.runEnabledTests
import khoury.testSame
import khoury.fileExists
import khoury.reactConsole
import java.io.File
import khoury.isAnInteger
import khoury.input
import khoury.CapturedResult
import khoury.captureResults
fileExists("questionBank.txt")
//how do i import "question.txt" into kotlin

//step 1:

data class Question(val question: String, val answer: String){

}

val q1 = Question("What is the capital of England?", "London")
val q2 = Question("What is 1+1?", "2")
val q3 = Question("What year was the Declaration of Independence signed?", "1776")
val q4 = Question("Where is the capital of America?", "Washington D.C.")
//Step 2: we make a string name and a list name
data class QuestionBank(val name: String, val listOfQuestions: List<Question>){

}
val listGeo = listOf(q1, q4)
val listMore = listOf(q2,q3)
val listBank = QuestionBank("Geography", listGeo)
val listBank2 = QuestionBank("Stuff", listMore)
val megaList = listOf(listBank, listBank2)

//step 3:

fun cubes(count: Int): QuestionBank{
    //we create a list of numbers
    fun toCube(num: Int): Question{
        val cubed = num*num*num
        return Question("What is $num cubed?", cubed.toString())
    }
    val listOfCount = (1..count).toList()
    // we make a new list of all items in listOfCount cubed, then we create a list of the Question class. We also turn the second parameter into a string
    // to satisfy the conditions of class Question.
    val listOfCubed = listOfCount.map(::toCube)
    
    //we return the type of question with the bank name and the list of the Question class.
   // return QuestionBank("Perfect Cubes", listOfCubed) -> will return it in weird format
   // making it readable in console:
   return QuestionBank("Perfect Cubes", listOfCubed)
   
   //returns questions and answers
}

//step 4.1:

fun questionToString(input: Question): String{
    return "${input.question} | ${input.answer}".toString()
}

//step 4.2

fun stringToQuestion(input: String): Question{
    val result = input.split("|")
    val partOne = result[0]
    val partTwo = result[1]
    return Question(partOne, partTwo)
    
}

//step 4.3
//read the file and questions
fun readQuestionBank(path: String): QuestionBank{
    val questions = File(path).readLines()
    val fileSeq = questions.map { stringToQuestion(path) } 
    return QuestionBank("File Read", fileSeq)
}

//readQuestionBank("questionBank.txt") 

//step 5.1


fun isCorrect(s: String): Boolean{
    if(s.startsWith("y".uppercase()) || s.startsWith("y".lowercase())){
        return true
    }
    return false
}

//step 5.2

fun studyQuestion(studyQuestion: Question){
    val initialState: String = "INIT"
    //step 1: if true, execute step 2 and exit loop, else continue
    fun terminate(currentState: String): Boolean {
        return currentState == studyQuestion.answer
    }

        //step 2:
    fun stateToText(currentState: String): String {
        if(currentState == "INIT"){ // return studyQuestion's question
            return("${studyQuestion.question}")
        }
        else if(currentState != "${studyQuestion.question}"){
            return "Try again! "
        }
        else{
            return("You Got it!")
        }
    }
    //step 3:
    fun transitionState(currentState: String, userInput: String): String {
            
        return "${userInput}"
    }

    reactConsole(
    initialState,
    ::stateToText,
    ::transitionState,
    ::terminate
    )
}
//studyQuestion(q1)

//step 6.1

data class QuestionBankState(val viewState: Boolean, val count: Int, val correct: Int){
    
}

//step 6.2(incomplete)

fun studyQuestionBank(studyQuestionBank: QuestionBank): QuestionBankState{
    val initialState = QuestionBankState(true, 0, 0)
    // State to text function to display the current question or the result
    fun bankState(currentState: QuestionBankState): String {
        if(currentState.count == studyQuestionBank.listOfQuestions.size){
            return "You got ${currentState.correct} numbers right!"
        }
        else if(currentState.count <= studyQuestionBank.listOfQuestions.size){

            return studyQuestionBank.listOfQuestions[currentState.count].question.toString()
        }
        else {
            //this line is suppose to run but for some reason it does not due to the conditionals I have in place
            return "The question is: ${studyQuestionBank.listOfQuestions[currentState.count].answer} \n  Was it correct? Input y/n"
        }
    }

    // tansition
    fun transitionState(currentState: QuestionBankState, userInput: String): QuestionBankState {
        if(isCorrect(userInput)){
            return QuestionBankState(viewState = false, currentState.count + 1,  currentState.correct + 1)
        }
        else{
            return QuestionBankState(viewState = false, currentState.count + 1, currentState.correct)
        }
        return QuestionBankState(viewState = true, currentState.count, currentState.correct)
    }
    

    // Terminate function to check if we've finished all questions
    fun terminate(currentState: QuestionBankState): Boolean {
        // terminates once we finish counting through
        return currentState.count == studyQuestionBank.listOfQuestions.size
    }

    
    return reactConsole(
        initialState,
        ::bankState,
        ::transitionState,
        ::terminate
    )
    // the question does not correctly output the numbers and the y/n. I have tried to fix it but unfortunately ran out of time.
}  

//step 7.

fun chooseBank(listOfQuestionBank: List<QuestionBank>): String{
    val menu = buildString { 
        append("Welcome to Question Time! You can choose from ${listOfQuestionBank.size} question banks:\n")
        listOfQuestionBank.mapIndexed { index, bank -> append("${index + 1}. ${bank.name}\n") }
        append("Enter your choice:\n")
        // we use mapindexed to created a new list with indexed name and then we convert to string
    }
    
    do{
        println(menu)
        //get input
        val userInput = input()
        // make int out of input, if equals then we return index where its at
        if(isAnInteger(userInput) && userInput.toInt() in 1..listOfQuestionBank.size){
            return listOfQuestionBank[userInput.toInt() - 1].name
        }
        else { 
            println("Invalid input. Please enter a valid number. ")
        }
    }while(true)
}


//step 8

fun play(){

    val fileBankPath = "questionBank.txt" // path to the file question bank
    
    // check if file exists, otherwise handle or skip it
    val fileBank = if (fileExists(fileBankPath)) readQuestionBank(fileBankPath) else null
    val generatedBank = cubes(5) // Generates a question bank of cubes from 1 to 5

    // questions
    val predefinedBank = QuestionBank("General Knowledge", listOf(
        Question("What is the capital of England?", "London"),
        Question("What is 1 + 1?", "2"),
        Question("What year was the Declaration of Independence signed?", "1776"),
        Question("Where is the capital of America?", "Washington D.C.")
    ))

    // Step 2: Collect available question banks in a list
    val questionBanks = mutableListOf(predefinedBank, generatedBank)
    if (fileBank != null) questionBanks.add(fileBank)

    // Step 3: Let the user choose a question bank
    val chosenBankName = chooseBank(questionBanks)
    //find the name of question of bank
    val chosenBank = questionBanks.find { it.name == chosenBankName } ?: return

    //Step 4: Study the chosen question bank and count correct answers
    val correctAnswers = studyQuestionBank(predefinedBank)
    //Since my studybank wont work, the output only gets back the first question name
    // Step 5: Display the results

    //correct answers would be what holds the studyQuestionBank final return value
    println("You got (correctAnswers) out of ${chosenBank.listOfQuestions.size} questions!")
}


@EnabledTest
fun isCorrect(){

    testSame(isCorrect("young"),true,"True case")
    testSame(isCorrect("Young"), true, "True Case")
    testSame(isCorrect("No"), false, "False case")
}
@EnabledTest
fun cubes(){

    testSame(cubes(3), 27, "True Case")
    testSame(cubes(1), 1, "True Case")
    testSame(cubes(2), 8, "True Case")
}

@EnabledTest
fun testquestionToString(){
    //returns correct
    testSame(questionToString(Question("Cube of 4", "4")), "Cube of 4 | 4", "True Case")
    testSame(questionToString(Question("Cube of 4", "4")), "Question(question = Cube of 4,   answer = 4", "False Case")
}
@EnabledTest
fun teststringToQuestion(){
    //returns question
    testSame(stringToQuestion("What is 2+2| 4"), Question("What is 2+2", "4"), "True Case")
}
@EnabledTest
fun testreadQuestionBank(){
    //for some reason returned empty, not sure why because it is suppose to open file and return the contents of it
    testSame(readQuestionBank("questionBank.txt"), listOf(Question("What is the capital of England?", "London"), Question("What is 2 + 2?", "4")), "True Case")
}

@EnabledTest
fun testchooseBank(){
    // true if user inputs valid number
    testSame(chooseBank(megaList), "Geography", "True Case")
    testSame(chooseBank(megaList), "Stuff", "True Case")
    testSame(chooseBank(megaList), "Invalid", "False Case")
}

/* 
Cannot get test to run, function on its own works
@EnabledTest
fun teststudyQuestion(){
    testSame(
        captureResults(
        ::studyQuestion,
        "4"
        
        ),
        CapturedResult(
        Question(question = "What is square root of 16", answer = "4"),
        "What is the square root of 16:",
        "The answer is 4"
        
        )
    )
}

@EnabledTest
fun teststudyQuestion(){
    testSame(
        captureResults(
        ::studyQuestionBank,
        "y",
        "n"
        
        ),
        CapturedResult(
        QuestionBankState(viewState = true, count = 0, correct = 2),
        return QuestionBankState(viewState = true, currentState.count, currentState.correct),
        return QuestionBankState(viewState = false, currentState.count, currentState.correct)
        
        )
    )
}

unsure how to do this test with captured results
*/

fun main(){
    //run functions and tests
    play()
    runEnabledTests(this)
    studyQuestionBank(listBank)
    studyQuestion(q1) // inf for loop
    
}


main()

