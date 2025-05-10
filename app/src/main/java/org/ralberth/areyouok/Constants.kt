package org.ralberth.areyouok

class Constants {
    companion object {
        /*
         * How many milliseconds are in a minute.
         * "60 * 1000" is real-life, but setting this to a smaller number
         * makes testing and development quicker.
         */
        val MS_PER_MIN: Long = 60 * 1000
//        val MS_PER_MIN: Long = 5 * 1000   // Pretend every 10 seconds is a minute
    }
}
