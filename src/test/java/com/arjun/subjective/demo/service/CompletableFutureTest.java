package com.arjun.subjective.demo.service;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 用`CompletableFuture`实现异步任务.
 *
 * @author arjun
 * @date 2021/01/20
 */
public class CompletableFutureTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(CompletableFutureTest.class);

    @Resource
    CompletableFutureTest completableFutureTest;

    public static void main(String[] args) throws Exception {

//        Future<String> completableFuture = calculateAsyncWithCancellation();
//        // 想获取CompletableFuture 的结果可以使用 CompletableFuture.get() 方法：
//        System.out.println(completableFuture.get());
//        runAsync();
//        supplyAsync();

    }


    /**
     * 接收一个Runnable参数，没有返回值.
     */
    public static void runAsync() {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            LOGGER.info("初始化CompletableFuture子任务！ runAsync");
        }, Executors.newFixedThreadPool(3));
    }


    /**
     * 无参数，会返回一个结果.
     */
    public static void supplyAsync() throws ExecutionException, InterruptedException {

        CompletableFuture<String> future = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                LOGGER.info("初始化CompletableFuture子任务！ supplyAsync");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
                return "Result of the asynchronous computation";
            }
        });

//        String name = null;
//
//        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
//            // long running task
//            if (name == null) {
//                throw new RuntimeException("error!");
//            } else {
//                return "whenComplete task";
//            }
//        });
//
//        CompletableFuture<String> completableFuture = future1.whenComplete((result, exception) -> {
//            if (null == exception) {
//                System.out.println("result from previous task: " + result);
//            } else {
//                System.err.println("Exception thrown from previous task: " + exception.getMessage());
//            }
//        });
//
//        System.out.println(completableFuture.get());

    }


//    handle();

//    handleError();

//    exceptionally2();

//    exceptionally();

//    anyOf();

//    allOf();

//    thenAcceptBoth();

//    thenCombine();

//    thenApply(future);

//    thenAccept(future);

//    thenRun(future);


    /**
     * handle() 从异常恢复，无论一个异常是否发生它都会被调用.
     */
    private static void handle() throws InterruptedException, ExecutionException {
        Integer age = -1;

        CompletableFuture<String> maturityFuture = CompletableFuture.supplyAsync(() -> {
            if (age < 0) {
                throw new IllegalArgumentException("Age can not be negative");
            }
            if (age > 18) {
                return "Adult";
            } else {
                return "Child";
            }
        }).handle((res, ex) -> {
            if (ex != null) {
                System.out.println("Oops! We have an exception - " + ex.getMessage());
                return "Unknown!";
            }
            return res;
        });

        System.out.println("Maturity : " + maturityFuture.get());
        // Prints - Maturity : Unknown!
    }

    public static void handleError() throws ExecutionException, InterruptedException {
        String name = null;

        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            if (name == null) {
                throw new RuntimeException("Computation error!");
            }
            return "Hello, " + name;
        }).handle((res, ex) -> res != null ? res : "Hello, Stranger!");

        System.out.println(completableFuture.get());
        // Prints - Hello, Stranger!
    }


    /**
     * 通过 .exceptionally() 方法处理了异常，并返回新的结果
     */
    private static void exceptionally() throws InterruptedException, ExecutionException {
        Integer age = -1;

        CompletableFuture<String> maturityFuture = CompletableFuture.supplyAsync(() -> {
            if (age < 0) {
                throw new IllegalArgumentException("Age can not be negative");
            }
            if (age > 18) {
                return "Adult";
            } else {
                return "Child";
            }
        }).exceptionally(ex -> {
            LOGGER.error("Oops! We have an exception - {}", ex.getMessage());
            return "Unknown!";
        });

        System.out.println("Maturity : " + maturityFuture.get());
    }

    private static void exceptionally2() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException();
        })
                .exceptionally(ex -> "ERROR - ResultA")
                .thenApply(resultA -> resultA + ", resultB")
                .thenApply(resultB -> resultB + ", resultC")
                .thenApply(resultC -> resultC + ", resultD");

        System.out.println(future1.join());
    }

    /**
     * anyOf() 当三个中的任何一个CompletableFuture完成， anyOfFuture就会完成.
     */
    private static void anyOf() throws InterruptedException, ExecutionException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Result of Future 1";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Result of Future 2";
        });

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Result of Future 3";
        });

        CompletableFuture<Object> anyOfFuture = CompletableFuture.anyOf(future1, future2, future3);

        System.out.println(anyOfFuture.get()); // Result of Future 2
    }


    /**
     * allOf() 允许等待所有的完成任务.
     */
    static CompletableFuture<String> downloadWebPage(String pageLink) {
        return CompletableFuture.supplyAsync(() -> {
            // TODO Code to download and return the web page's content
            return null;
        });
    }

    private static void allOf() throws ExecutionException, InterruptedException {
        List<String> webPageLinks = Lists.newArrayList();    // A list of 100 web page links

        // Download contents of all the web pages asynchronously
        List<CompletableFuture<String>> pageContentFutures = webPageLinks.stream()
                .map(webPageLink -> downloadWebPage(webPageLink))
                .collect(Collectors.toList());


        // Create a combined Future using allOf()
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                pageContentFutures.toArray(new CompletableFuture[pageContentFutures.size()])
        );

        // When all the Futures are completed, call `future.join()` to get their results and collect the results in a list -
        CompletableFuture<List<String>> allPageContentsFuture = allFutures.thenApply(v -> {
            return pageContentFutures.stream()
                    //.map(pageContentFuture -> pageContentFuture.join())
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
        });

        // Count the number of web pages having the "CompletableFuture" keyword.
        CompletableFuture<Long> countFuture = allPageContentsFuture.thenApply(pageContents -> {
            return pageContents.stream()
                    .filter(pageContent -> pageContent.contains("CompletableFuture"))
                    .count();
        });

        System.out.println("Number of Web Pages having CompletableFuture keyword - " + countFuture.get());
    }


    /**
     * 当你想要使用两个Future结果时，但不需要将任何结果值进行返回时.
     */
    private static void thenAcceptBoth() {
        CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> "Hello")
                .thenAcceptBoth(CompletableFuture.supplyAsync(() -> " World"), (s1, s2) -> System.out.println(s1 + s2));
    }


    /**
     * thenCombine()被用来当两个独立的Future都完成的时候，用来做一些事情.
     */
    private static void thenCombine() throws InterruptedException, ExecutionException {
        System.out.println("Retrieving weight.");
        CompletableFuture<Double> weightInKgFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return 65.0;
        });

        System.out.println("Retrieving height.");
        CompletableFuture<Double> heightInCmFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return 177.8;
        });

        System.out.println("Calculating BMI.");
        CompletableFuture<Double> combinedFuture = weightInKgFuture
                .thenCombine(heightInCmFuture, (weightInKg, heightInCm) -> {
                    Double heightInMeter = heightInCm / 100;
                    return weightInKg / (heightInMeter * heightInMeter);
                });

        System.out.println("Your BMI is - " + combinedFuture.get());
    }


    /**
     * `thenCompose`将前一个Future的返回结果作为后一个操作的输入.
     */
    private static CompletableFuture<Double> thenCompose() throws ExecutionException, InterruptedException {
        CompletableFuture<Double> userId = getUsersDetail("userId")
                .thenComposeAsync(user -> getCreditRating(user));
        System.out.println(userId.get());
        return userId;
    }

    static CompletableFuture<String> getUsersDetail(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            return userId;
        });
    }

    static CompletableFuture<Double> getCreditRating(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            return 5D;
        });
    }


    /**
     * thenRun() 不接收，不返回
     */
    private static void thenRun(CompletableFuture<String> future) throws InterruptedException, ExecutionException {
        CompletableFuture<Void> completableFuture = future.thenRunAsync(() -> System.out.println("Computation finished."));
        System.out.println(completableFuture.get());
        // Prints - null
    }


    /**
     * thenAccept() 接收结果，不返回
     */
    private static void thenAccept(CompletableFuture<String> future) throws InterruptedException, ExecutionException {
        CompletableFuture<Void> completableFuture = future.thenAcceptAsync(resultA -> {
            LOGGER.info("Computation returned: {}", resultA);
        });

        System.out.println(completableFuture.get());
        // Prints - null
    }


    /**
     * thenApply() 接收结果，返回结果
     */
    private static void thenApply(CompletableFuture<String> future) throws InterruptedException, ExecutionException {
        CompletableFuture<String> completableFuture = future.thenApplyAsync(resultA -> {
            LOGGER.info(resultA);
            return "Hello " + resultA;
        }).thenApplyAsync(resultB -> {
            LOGGER.info(resultB);
            return resultB + ", Welcome to the arjun Blog";
        });

        System.out.println(completableFuture.get());
        // Prints - Hello Result of the asynchronous computation, Welcome to the arjun Blog
    }


    /**
     * 通过将结果提供给完整方法来完成Future.
     */
    public Future<String> calculateAsync() {
        // 创建 CompletableFuture
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(500);
            // 使用 CompletableFuture.complete() 手工的完成一个 Future
            completableFuture.complete("Hello");
            return null;
        });

        return completableFuture;
    }

    /**
     * 通过Future的取消方法完成.
     */
    public static Future<String> calculateAsyncWithCancellation() {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(500);
            completableFuture.cancel(false);
            return null;
        });
        return completableFuture;
    }

    /**
     * CompletableFuture的completedFuture方法来直接返回一个Future.
     */
    public static Future<String> useCompletableFuture() {
        Future<String> completableFuture =
                CompletableFuture.completedFuture("Hello");
        return completableFuture;
    }

    // lambda 表达式。
    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
        System.out.println("初始化CompletableFuture子任务！ supplyAsync");
//            int i = 1 / 0;
        return "hello CompletableFuture";
    });


    CompletableFuture<String> future1 = future.thenApplyAsync(t -> {
        System.out.println("================thenApplyAsync==================");
        System.out.println("上一个任务的返回结果集t: " + t);
        return "hello thenApplyAsync";
    }).thenCombineAsync(CompletableFuture.supplyAsync(() -> {
        System.out.println("================这是第二任务==================");
        return "hello CompletableFuture2";
    }), (t, u) -> {
        System.out.println("================这是一个新的任务==================");
        System.out.println("第一个任务的返回结果集t: " + t);
        System.out.println("第二个任务的返回结果集u: " + u);
        return "hello CompletableFuture2";
    });


//        future.thenAcceptAsync(t -> {
//            System.out.println("================thenAcceptAsync==================");
//            System.out.println("上一个任务的返回结果集t: " + t);
//        });
//        future.thenRunAsync(() -> {
//            System.out.println("================thenRunAsync=====================");
//        });


//                .whenCompleteAsync((t, u) -> {
//            System.out.println("================whenCompleteAsync===============");
//            System.out.println("上一个任务的返回结果集t: " + t);
//            System.out.println("上一个任务的异常u: " + u);
//        }).exceptionally(t -> {
//            System.out.println("==================exceptionally=================");
//            System.out.println("上一个任务的异常t: " + t);
//            return "hello exceptionally!";
//        }).handleAsync((t, u) -> {
//            System.out.println("================handleAsync-====================");
//            System.out.println("上一个任务的返回结果集t: " + t);
//            System.out.println("上一个任务的异常u: " + u);
//            return "hello handleAsync";
//        });


}
