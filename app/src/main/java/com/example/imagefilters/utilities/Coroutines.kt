package com.example.imagefilters.utilities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// object 用於創建單例對象。在這裡，Coroutines 是一個包含靜態函數的單例對象。
object Coroutines {

    // fun io(work: suspend(() -> Unit)) 定義了一個名為 io 的函數，該函數接受一個 suspend 函數作為參數。
    fun io(work: suspend(() -> Unit)) =

        // CoroutineScope(Dispatchers.IO) 創建了一個協程範圍，
        // 使用 Dispatchers.IO 調度器，表示這個協程將運行在 I/O 操作的背景執行緒上。
        CoroutineScope(Dispatchers.IO).launch { work() }
        // .launch { work() } 啟動了一個新的協程，執行傳入的 work 函數。這是一個非阻塞的協程啟動方式。
        // work 參數的類型是 suspend () -> Unit，表示這是一個可以被暫停的函數。


    // 簡而言之，這個 Coroutines 物件提供了一個簡單的方式來在 I/O 調度器上運行非阻塞的協程。
    // 這是一個通用的工具，可用於在背景執行緒上執行需要耗時操作的程式碼，同時保持應用的反應性。


    /*
    * 非阻塞的協程啟動方式：
    在 Kotlin 的協程中，非阻塞通常指的是在不阻塞主執行緒的情況下啟動協程，使協程可以在背後執行而不影響主執行緒的進行。
    使用 launch 函數或其他啟動協程的機制，可以在背景執行非同步操作，而主執行緒可以繼續執行其他操作，而不必等待協程完成。

    * 非同步：
    非同步是一種程式設計的概念，指的是在執行某些操作時不必等待該操作完成，而可以繼續執行其他操作。這通常與多任務處理和異步編程相關。
    在 Java 中，非同步的概念可以透過使用執行緒（Thread）、CompletableFuture、Callback 或 Future 等機制實現。
    在 Kotlin 中，協程提供了一種更簡潔且易於理解的方式來處理非同步操作，而不需要明確的回調（Callback）。
    簡而言之，非阻塞的協程啟動方式是 Kotlin 協程特有的概念，強調在協程中進行非同步操作而不阻塞主執行緒。
    * 而非同步則是一種更廣泛的概念，指的是處理操作時不必等待其完成。在 Kotlin 中，非同步通常是透過協程來實現的。
    * */
}