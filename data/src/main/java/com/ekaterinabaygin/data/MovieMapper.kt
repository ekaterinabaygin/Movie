package com.ekaterinabaygin.data


class MovieMapper {
    fun mapDtoToEntity(dto: MoviePreviewDto): MovieEntity {
        return MovieEntity(
            id = dto.id,
            author = dto.author,
            content = dto.content,
            url = dto.url,
            posterPath = dto.posterPath
        )
    }
}
