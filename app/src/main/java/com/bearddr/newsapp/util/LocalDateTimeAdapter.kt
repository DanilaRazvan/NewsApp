package com.bearddr.newsapp.util

import com.bearddr.newsapp.R
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class LocalDateTimeJsonAdapter @Inject constructor(
  private val dateFormatter: DateFormatter
) {
  @FromJson
  fun fromJson(value: String): LocalDateTime {
    return dateFormatter.stringToDate(value)
  }

  @ToJson
  fun toJson(value: LocalDateTime): String {
    return dateFormatter.dateToString(value)
  }
}

class DateFormatter @Inject constructor(
  private val resourceHelper: ResourceHelper
) {

  private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter
    .ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    .withZone(ZoneId.of("UTC"))

  fun dateToString(date: LocalDateTime): String =
    dateTimeFormatter.format(date.atZone(ZoneOffset.UTC))

  fun stringToDate(date: String?): LocalDateTime = LocalDateTime.parse(date, dateTimeFormatter)

  fun dateToZone(date: LocalDateTime): LocalDateTime = date
    .atOffset(ZoneOffset.UTC)
    .atZoneSameInstant(ZoneId.systemDefault())
    .toLocalDateTime()


  private val months = arrayOf(
    resourceHelper.getString(R.string.month_abbreviation_january),
    resourceHelper.getString(R.string.month_abbreviation_february),
    resourceHelper.getString(R.string.month_abbreviation_march),
    resourceHelper.getString(R.string.month_abbreviation_april),
    resourceHelper.getString(R.string.month_abbreviation_may),
    resourceHelper.getString(R.string.month_abbreviation_june),
    resourceHelper.getString(R.string.month_abbreviation_july),
    resourceHelper.getString(R.string.month_abbreviation_august),
    resourceHelper.getString(R.string.month_abbreviation_september),
    resourceHelper.getString(R.string.month_abbreviation_october),
    resourceHelper.getString(R.string.month_abbreviation_november),
    resourceHelper.getString(R.string.month_abbreviation_december),
  )

  fun dateToSimpleString(date: LocalDate): String = when (date) {
    LocalDate.now().minusDays(3) -> resourceHelper.getString(R.string.date_3_days_ago)
    LocalDate.now().minusDays(2) -> resourceHelper.getString(R.string.date_2_days_ago)
    LocalDate.now().minusDays(1) -> resourceHelper.getString(R.string.date_yesterday)
    LocalDate.now() -> resourceHelper.getString(R.string.date_today)
    else -> {
      var res = ""
      res += when (date.dayOfMonth) {
        1 -> "1 "
        2 -> "2 "
        3 -> "3 "
        else -> "${date.dayOfMonth} "
      }
      res += months[date.monthValue - 1]

      res
    }
  }

  fun timeToSimpleString(time: LocalTime): String {
    val h = if (time.hour < 10) "0${time.hour}" else "${time.hour}"
    val m = if (time.minute < 10) "0${time.minute}" else "${time.minute}"

    return "$h:$m"
  }
}