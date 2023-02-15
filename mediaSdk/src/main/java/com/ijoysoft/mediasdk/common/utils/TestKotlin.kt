package com.ijoysoft.mediasdk.common.utils

import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TestKotlin {
    suspend fun fetchData(): String {
        delay(1000L)
        return "Hello world"
    }

    suspend fun test2(): Int {
        return 1;
    }

    @Test
    fun returnValue() = runBlocking {
        val p = runBlocking {
            2
        }
        val p2 = runBlocking {
            "aaa"
        }
        System.out.println("p:$p   p2:$p2")
    }

    @Test
    fun dataShouldBeHelloWorld() = runTest {
        val list = mutableListOf<String>("a", "a", "a", "a", "a")
        //withContext(Dispatchers.IO){
        //    for (i in 0 until 10000){
        //        list.add(i.toString());
        //    }
        //}
        //withContext(Dispatchers.IO){
        //    list.forEach {
        //        println(it)
        //    }
        //}

        var flag = false;
        var i = 0;
        while (i < 100) {
            i++;
            flag = false
            runBlocking {
                delay(10)
                if (i % 10 == 0) {
                    System.out.println("$i")
                    flag = true
                    return@runBlocking
                }

            }
            if (flag) {
                continue
            }
            System.out.println("-------$i--------")
        }
        //assertEquals("Hello waorld", data)
    }

    @Test
    fun cancelTest() = runBlocking {
        val job = launch(context = Dispatchers.Main) {
            //try {
            //repeat(200) {
            //    println("job: I am sleeping $it")
            //    delay(500)
            //}


            //var i = 0;
            //while (i < 20) {
            //    println("job: I am sleeping $i")
            //    delay(500)
            //    i++;
            //}

            var nextActionTime = System.currentTimeMillis()
            var i = 0
            while (i < 20) {
                if (System.currentTimeMillis() >= nextActionTime) {
                    println("Job: ${i++}")
                    nextActionTime += 500
                }
            }
            //} catch (e: CancellationException) {
            //    println("canceled")
            //} finally {
            //    println("finally块")
            //}
        }

        delay(1300)
        println("hello")

        job.cancelAndJoin()
        println("welcome")
    }
}