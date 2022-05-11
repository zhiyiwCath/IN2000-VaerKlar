package no.uio.g19.klesapp.data.model.wardrobe

/**
 * Antrekk is a data class that represents an outfit
 *
 * @param id is an unique identifier of the outfit
 * @param ikon is the name of the image file associated with the outfit
 * @param plagg is a list of id-strings representing the clothes the outfit consists of
 * @param tempMin is the lowest acceptable temperature in which this outfit will be recommended
 * @param vind is a boolean that represents whether the outfit is suited for windy conditions
 * @param maxNedbor is the highest amount of precipitation in which the outfit will be recommended
 */
data class Antrekk(val id: String,
                   val ikon: String,
                   val plagg: List<String>,
                   val tempMin: Double,
                   val vind: Boolean,
                   val maxNedbor: Int)

