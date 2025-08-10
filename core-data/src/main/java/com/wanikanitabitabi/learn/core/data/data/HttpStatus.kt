package com.wanikanitabitabi.learn.core.data.data

enum class HttpStatus(val code: Int) {
    CONTINUE(100),
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NO_CONTENT(204),
    MOVED_PERMANENTLY(301),
    FOUND(302),
    SEE_OTHER(303),
    NOT_MODIFIED(304),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    UNPROCESSABLE_ENTITY(422),
    TOO_MANY_REQUESTS(429),
    INTERNAL_SERVER_ERROR(500),
    NOT_IMPLEMENTED(501),
    BAD_GATEWAY(502),
    SERVICE_UNAVAILABLE(503),
    GATEWAY_TIMEOUT(504);

    companion object {
        private val map = entries.associateBy(HttpStatus::code)
        fun fromCode(code: Int) = map[code]
    }
}

