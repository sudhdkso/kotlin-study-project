package com.study.boardproject.board.service

import com.study.boardproject.board.dto.request.BoardRequestDto
import com.study.boardproject.board.dto.response.BoardResponseDto
import com.study.boardproject.board.entity.Board
import com.study.boardproject.board.repository.BoardRepository
import com.study.boardproject.board.repository.getByBoardId
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BoardService(
    val boardRepository: BoardRepository,
    val userService: UserService
) {
    // 생성
    fun save(requestDto: BoardRequestDto): BoardResponseDto {
        val user = userService.findUserByEmail(requestDto.email)

        validateRequest(requestDto)
        val board = boardRepository.save(Board(requestDto, user))

        return BoardResponseDto(board, user)
    }

    fun findByBoardId(boardId: Long): Board = boardRepository.getByBoardId(boardId)

    //게시글 개별 조회
    fun findOne(boardId: Long): BoardResponseDto {
        val board = findByBoardId(boardId)
        return convertBoardToBoardResponse(board)
    }

    //게시글 전체 조회는 최근 작성순 or 조회순 두가지
    fun findAll(pageable: Pageable) : List<BoardResponseDto> {
        val boardResponseList: MutableList<BoardResponseDto>  = mutableListOf()

        for(board in boardRepository.findAll(pageable)) {
            boardResponseList.add(convertBoardToBoardResponse(board))
        }

        return boardResponseList.toList()
    }

    //업데이트
    fun update(boardId: Long, requestDto: BoardRequestDto): BoardResponseDto {
        val user = userService.findUserByEmail(requestDto.email)

        val board = findByBoardId(boardId)

        validateRequest(requestDto)
        board.update(requestDto)

        return convertBoardToBoardResponse(boardRepository.save(board))
    }

    //삭제
    fun deleteByBoardId(boardId: Long) = boardRepository.deleteById(boardId)

    fun search(query: String) : List<BoardResponseDto> {
        val boardResponseList: MutableList<BoardResponseDto>  = mutableListOf()

        for(board in boardRepository.searchByTitleOrContent(query)) {
            boardResponseList.add(convertBoardToBoardResponse(board))
        }

        return boardResponseList.toList()
    }

    fun viewCountup(boardId: Long) {
        val board = findByBoardId(boardId)
        board.viewCountUp()
        boardRepository.save(board)
    }

    fun convertBoardToBoardResponse(board: Board) : BoardResponseDto
    = BoardResponseDto(board, board.writer!!)

    private fun validateRequest(request: BoardRequestDto) {
        require(request.title.isNotBlank() && request.content.isNotBlank()) {
            "작성하지 않은 항목이 존재합니다."
        }
    }
}


