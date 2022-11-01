package com.aayush.runningappaayush.repositories

import com.aayush.runningappaayush.db.Run
import com.aayush.runningappaayush.db.RunDao
import javax.inject.Inject

/*

    job of repository is to collect our data from all different data sources

 */
class MainRepository @Inject constructor(
    val runDao:RunDao,
){

    suspend fun insertRun(run:Run) = runDao.insertRun(run)

    suspend fun deleteRun(run:Run) = runDao.deleteRun(run)

    fun getALlRunsSortedByDate() = runDao.getAllRunsSortedByDate() //since live data is async anyways so this function is not suspend fun

    fun getALlRunsSortedByDistance() = runDao.getAllRunsSortedBydistanceInMeters() //since live data is async anyways so this function is not suspend fun

    fun getALlRunsSortedByTimeInMillis() = runDao.getAllRunsSortedByTimeMillis() //since live data is async anyways so this function is not suspend fun

    fun getALlRunsSortedByAvgSpeed() = runDao.getAllRunsSortedByavgSpeedInKMH() //since live data is async anyways so this function is not suspend fun

    fun getALlRunsSortedByCaloriesBurned() = runDao.getAllRunsSortedByCaloriesBurned() //since live data is async anyways so this function is not suspend fun

    fun getTotalAvgSpeed() = runDao.getTotalAvgSpeed()

    fun getTotalDistance() = runDao.getTotalDistance()

    fun getTotalCaloriesBurned() = runDao.getTotalcaloriesBurned()

    fun getTotalTimeInMillis() = runDao.getTotalTimeInMillis()

}