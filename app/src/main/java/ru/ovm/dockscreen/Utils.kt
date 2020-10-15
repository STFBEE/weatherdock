package ru.ovm.dockscreen

object Utils {
    fun toCelsius(kelvin: Double) = (kelvin - 273.15).toInt()
    fun getIcon(code: String) = icons[code] ?: R.drawable.mostly_sunny

    private val icons = mapOf(
        "01d" to R.drawable.mostly_sunny,
        "02d" to R.drawable.party_cloudy,
        "03d" to R.drawable.party_cloudy,
        "04d" to R.drawable.mostly_cloudy,
        "09d" to R.drawable.sleet,
        "10d" to R.drawable.rain,
        "11d" to R.drawable.severe_thunderstorm,
        "13d" to R.drawable.snow,
        "50d" to R.drawable.fog,
        "01n" to R.drawable.clear_night,
        "02n" to R.drawable.party_cloudy_night,
        "03n" to R.drawable.party_cloudy_night,
        "04n" to R.drawable.mostly_cloudy_night,
        "09n" to R.drawable.sleet_night,
        "10n" to R.drawable.rain_nght,
        "11n" to R.drawable.severe_thunderstorm_night,
        "13n" to R.drawable.snow_night,
        "50n" to R.drawable.fog_night,
    )
}