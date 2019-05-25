package kz.mircella.blogapplication;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.Function;
import kz.mircella.blogapplication.blogpost.model.BlogPost;
import kz.mircella.blogapplication.blogpost.model.BlogPostDao;

public class UtilClass {

    @Test
    public void util(final BlogPostDao blogPostDao) {
        Observable<List<BlogPost>> blogPosts = Observable.fromCallable(new Callable<List<BlogPost>>() {
            @Override
            public List<BlogPost> call() throws Exception {
                return blogPostDao.getAll();
            }
        });
        blogPosts.concatMap(new Function<List<BlogPost>, ObservableSource<List<BlogPost>>>() {
            @Override
            public ObservableSource<List<BlogPost>> apply(List<BlogPost> blogPosts) throws Exception {
                if(blogPosts.isEmpty()) {

                }
                blogPostDao.insertAll(blogPosts.toArray(new BlogPost[blogPosts.size()]));
                return Observable.just(blogPosts);

            }
        });
    }
}
