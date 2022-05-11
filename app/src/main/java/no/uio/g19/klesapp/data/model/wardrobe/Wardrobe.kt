package no.uio.g19.klesapp.data.model.wardrobe

/**
 * A wardorbe is a data class used by the Database class to store outfits and clothes
 *
 * @param outfits is a list of all outfits in the wardrobe
 * @param clothes is a list of all the clothes in the wardrobe
 */
data class Wardrobe (val outfits: List<Antrekk>,
                     val clothes: List<Plagg>)
