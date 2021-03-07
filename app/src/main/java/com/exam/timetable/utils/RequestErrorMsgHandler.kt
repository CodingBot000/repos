package com.exam.timetable.utils

import java.lang.NumberFormatException

object RequestErrorMsgHandler {
    fun RequestErrorMsg(httpCode:String, requestType:String) {
        var showMsg = ""
        val statusCodeArr = httpCode.split(" ")
        if (statusCodeArr.size < 2)
            return
        try {
            val statusCode = statusCodeArr[1].toInt()
            when (requestType) {
                Const.REQUEST_TYPE_TIMETABLE_GET -> {
                    when (statusCode) {
                        400 -> showMsg = "$statusCode 클라이언트 요청 오류"
                        403 -> showMsg = "$statusCode x-api-key 인증 에러, URL경로, HTTP method오류"
                        500 -> showMsg = "$statusCode 서버 오류"
                    }

                }
                Const.REQUEST_TYPE_TIMETABLE_ADD, Const.REQUEST_TYPE_TIMETABLE_REMOVE
                -> {
                    when (statusCode) {
                        400 -> showMsg = "$statusCode 클라이언트 요청 오류 user_key, code가 없거나 빈값"
                        403 -> showMsg = "$statusCode x-api-key 인증 에러, URL경로, HTTP method오류, 유효하지 않은 ID토큰"
                        409 -> showMsg = "$statusCode 이미 등록되어 추가 등록이 불가합니다."
                        422 -> showMsg = "$statusCode 이미 삭제된 데이터입니다."
                        500 -> showMsg = "$statusCode 서버 오류"
                    }

                }
                Const.REQUEST_TYPE_MEMO_GET -> {
                    when (statusCode) {
                        400 -> showMsg = "$statusCode 클라이언트 요청 오류"
                        403 -> showMsg = "$statusCode x-api-key 인증 에러, URL경로, HTTP method오류, 유효하지 않은 ID토큰"
                        500 -> showMsg = "$statusCode 서버 오류"
                    }
                }
                Const.REQUEST_TYPE_MEMO_ADD
                -> {
                    when (statusCode) {
                        400 -> showMsg = "$statusCode 클라이언트 요청 오류 요청변수가 공백이거나 없음"
                        403 -> showMsg = "$statusCode x-api-key 인증 에러, URL경로, HTTP method오류, 유효하지 않은 ID토큰"
                        409 -> showMsg = "$statusCode 이미 등록된 메모 타입이거나 해당 메모의 강의가 시간표에 등록되지않았습니다"
                        422 -> showMsg = "$statusCode 형식에 맞지 않는 강의 코드, 메모 타입, 메모 날짜입니다."
                        500 -> showMsg = "$statusCode 서버 오류"
                    }
                }
                Const.REQUEST_TYPE_MEMO_REMOVE
                -> {
                    when (statusCode) {
                        400 -> showMsg = "$statusCode 클라이언트 요청 오류 user_key, code가 없거나 빈값"
                        403 -> showMsg = "$statusCode x-api-key 인증 에러, URL경로, HTTP method오류, 유효하지 않은 ID토큰"
                        422 -> showMsg = "$statusCode 형식에 맞지 않는 강의 코드, 메모 타입"
                        500 -> showMsg = "$statusCode 서버 오류"
                    }
                }
            }

            if (showMsg.isNotEmpty())
                toastMsg(showMsg)
        } catch (e :NumberFormatException) {

        }

    }
}