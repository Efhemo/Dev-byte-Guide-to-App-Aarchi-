
package com.example.android.devbyteviewer.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.devbyteviewer.database.getDatabase
import com.example.android.devbyteviewer.repository.VideosRepository
import retrofit2.HttpException


/**WorkManager is intended for tasks that are deferrable—that is,
 * not required to run immediately—and required to run reliably even if the app exits or
 * the device restarts. For example:

-Sending logs or analytics to backend services
-Periodically syncing application data with a server
 *
 */
class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
        CoroutineWorker(appContext, params) {


    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    /**
     * A coroutine-friendly method to do your work.
     */
    override suspend fun doWork(): Payload {
        val database = getDatabase(applicationContext)
        val repository = VideosRepository(database)
        return try {
            repository.refreshVideos()
            Payload(Result.SUCCESS)

            //whenever there's httpException from retrofit,
            // retry this job in the future
        } catch (e: HttpException) {
            Payload(Result.RETRY)
        }
    }
}