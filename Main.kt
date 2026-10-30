import java.util.*

object ChickenEggDispute {
    private val lock = Any()
    private var lastWord: String? = null

    private fun say(word: String?, times: Int) {
        val rnd = Random()
        for (i in 0..<times) {
            synchronized(lock) {
                println(word)
                lastWord = word // последний вошедший в synchronized — победитель
            }
            try {
                Thread.sleep(rnd.nextInt(80).toLong()) // случайная пауза -> спор непредсказуем
            } catch (ignored: InterruptedException) {
            }
        }
    }

    @Throws(InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val chicken = Thread(Runnable { say("Курица", 5) }, "ChickenThread")
        val egg = Thread(Runnable { say("Яйцо", 5) }, "EggThread")

        chicken.start()
        egg.start()

        // 1) Демонстрация isAlive(): пока кто-то жив — сообщаем состояние
        while (chicken.isAlive() || egg.isAlive()) {
            println(
                ("[main] Курица alive=" + chicken.isAlive()
                        + ", Яйцо alive=" + egg.isAlive())
            )
            Thread.sleep(100)
        }

        // 2) join() — гарантируем, что оба потока точно завершены
        chicken.join()
        egg.join()

        // 3) Подводим итог
        println("\nРезультат спора")
        println("Последнее слово сказал: " + lastWord)
        if ("Курица" == lastWord) {
            println("Вывод: сначала появилась КУРИЦА!")
        } else {
            println("Вывод: сначала появилось ЯЙЦО!")
        }
    }
}