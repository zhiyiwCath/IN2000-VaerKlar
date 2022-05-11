package no.uio.g19.klesapp.data.model.wardrobe

/**
 * Plagg is a data class that represent a piece of clothing
 *
 * @param id is an unique identifier of the piece of clothing
 * @param navn is the name of the piece of clothing
 * @param ikon is the name of the image file associated with the piece of clothing
 */
data class Plagg(val id: String,
                 val navn: String,
                 val ikon: String)

