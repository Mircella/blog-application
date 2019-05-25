package kz.mircella.blogapplication

import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kz.mircella.blogapplication.blogpost.dto.BlogPostDto
import org.apache.commons.io.IOUtils
import org.junit.Test
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class IntegrationTest {
    companion object {
        val ALMATY_ZONE_ID = ZoneId.of("Asia/Almaty")
        val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    }
    val fakeBackendServer = FakeBackendServer()

    @Test
    fun testBlogPostApiService() {
        val allBlogsDisposableName = "AllBlogsDisposable"
        val allBlogsDisposable = fakeBackendServer.getAllBlogs()
                .map {
                    convertToBlogPostDto(it.byteStream())
                }
//                .subscribe(
//                        {
//                            onNext("$allBlogsDisposableName from subscription", it)
//                        }, {
//                    onError("$allBlogsDisposableName from subscription", it)
//                }, {
//                    onComplete("$allBlogsDisposableName from subscription")
//                }, {
//                    onSubscribe("$allBlogsDisposableName from subscription", it)
//                })

        val allUsersDisposableName = "AllUsersDisposable"
        val allUsersDisposable = fakeBackendServer.getAllUsers()
                .map {
                    transformUser(it)
                }
//                .subscribe({
//                    onNext("$allUsersDisposableName from subscription", it)
//                }, {
//                    onError("$allUsersDisposableName from subscription", it)
//                }, {
//                    onComplete("$allUsersDisposableName from subscription")
//                }, {
//                    onSubscribe("$allUsersDisposableName from subscription", it)
//                })
        val generalDisposableName = "GeneralDisposable"
        Observable.zip(allBlogsDisposable, allUsersDisposable, BiFunction<BlogPostDto, FakeBackendServer.UserDto, FakeBackendServer.User> { t1, t2 -> FakeBackendServer.User(t1.authorId,  t2.username,t2.age)})
//        Observable.merge(allBlogsDisposable, allUsersDisposable)
        .subscribe({
            onNext(generalDisposableName, it)
        },{
            onError(generalDisposableName, it)
        },{
            onComplete(generalDisposableName)
        },{
            onSubscribe(generalDisposableName, it)
        })



    }

    private fun transformUser(user: FakeBackendServer.User): FakeBackendServer.UserDto {
        return FakeBackendServer.UserDto(listOf(user.firstname, user.surname).joinToString(separator = " "), user.age)
    }

    @Throws(IOException::class)
    fun convertToBlogPostDto(inputStream: InputStream, charset: Charset = StandardCharsets.UTF_8): BlogPostDto {
        val blogPostResponseString = IOUtils.toString(inputStream, charset)
        val gsonConverter = Gson()
        return gsonConverter.fromJson(blogPostResponseString, BlogPostDto::class.java)
    }

    private fun onComplete(subscriptionName: String) {
        val message = "Completed $subscriptionName"
        println("Time: ${getTime()}. Message: $message")
    }

    private fun onTerminate(subscriptionName: String) {
        val message = "OnTerminate $subscriptionName"
        println("Time: ${getTime()}. Message: $message")
    }

    private fun onDispose(subscriptionName: String) {
        val message = "OnDispose $subscriptionName"
        println("Time: ${getTime()}. Message: $message")
    }

    private fun onFinally(subscriptionName: String) {
        val message = "OnFinally $subscriptionName"
        println("Time: ${getTime()}. Message: $message")
    }

    private fun onSubscribe(subscriptionName: String, obj: Any) {
        val message = "OnSubscribe $subscriptionName"
        println("Time: ${getTime()}. Message: $message. Object: $obj")
    }

    private fun onNext(subscriptionName: String, obj: Any) {
        val message = "OnNext $subscriptionName"
        println("Time: ${getTime()}. Message: $message. Object: $obj")
    }

    private fun onAfterNext(subscriptionName: String, obj: Any) {
        val message = "OnAfterNext $subscriptionName"
        println("Time: ${getTime()}. Message: $message. Object: $obj")
    }

    private fun onEach(subscriptionName: String, obj: Any) {
        val message = "OnEach $subscriptionName"
        println("Time: ${getTime()}. Message: $message. Object: $obj")
    }

    private fun onError(subscriptionName: String, error: Throwable) {
        val message = "OnError $subscriptionName"
        println("Time: ${getTime()}. Message: $message. Error: ${error.localizedMessage}")
    }

    private fun getTime() = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ALMATY_ZONE_ID).toLocalDateTime().format(DATE_TIME_FORMATTER)
}