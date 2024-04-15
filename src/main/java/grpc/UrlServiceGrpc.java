package grpc;

import entities.Url;
import entities.User;
import entities.Statistic;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import services.UrlService;
import services.UserService;
import urlshortener.UrlRnGrpc;
import urlshortener.UrlRnOuterClass;
import urlshortener.UrlRnOuterClass.UrlListResponse;
import urlshortener.UrlRnOuterClass.UrlResponse;
import urlshortener.UrlRnOuterClass.UrlRequest;
import urlshortener.UrlRnOuterClass.CreateUrlRequest;

import java.util.Date;
import java.util.List;

public class UrlServiceGrpc extends UrlRnGrpc.UrlRnImplBase {

    private  UserService userService;
    private  UrlService urlService;

    public UrlServiceGrpc() {
        this.userService = new UserService();
        this.urlService = new UrlService();
    }


    @Override
    public void listUserUrls(UrlRnOuterClass.UserUrlsRequest request, StreamObserver<UrlListResponse> responseObserver) {
        String username = request.getUsername();
        User user = userService.findByUsername(username);

        if(user != null) {
            List<Url> userUrls = urlService.findByUser(user);
            UrlListResponse.Builder response = UrlListResponse.newBuilder();
            for(Url url : userUrls) {
                UrlRnOuterClass.StatisticList.Builder statistic = UrlRnOuterClass.StatisticList.newBuilder();
                for (Statistic stat : url.getStatistics()) {
                    UrlRnOuterClass.Statistic statisticBuilder = UrlRnOuterClass.Statistic.newBuilder()
                            .setBrowser(stat.getBrowser())
                            .setOperatingSystem(stat.getOperatingSystem())
                            .setIpDirection(stat.getIpDirection())
                            .build();
                    statistic.addStatistic(statisticBuilder);
                }
                UrlRnOuterClass.UrlResponse urlResponse = UrlRnOuterClass.UrlResponse.newBuilder()
                        .setUrl(url.getUrl())
                        .setOriginalUrl(url.getOriginalUrl())
                        .setDate(url.getDate().toString())
                        .setStatistic(statistic.build())
                        .build();

                response.addUrls(urlResponse);

            }
            responseObserver.onNext(response.build());

        }else {
            responseObserver.onError(Status.NOT_FOUND.withDescription("User not found").asRuntimeException());
            return;
        }
        responseObserver.onCompleted();

    }

    @Override
    public void createUrl(CreateUrlRequest request, StreamObserver<UrlResponse> responseObserver) {
        String username = request.getUsername();
        String url = request.getOriginalUrl();
        User user = userService.findByUsername(username);

        if (user != null) {
            String shortUrl = urlService.generateShortUrl(url);
            Url newUrl = new Url(shortUrl, url, new Date(), user, true, 0);
            urlService.createDb(newUrl);

            UrlRnOuterClass.UrlResponse urlResponse = UrlRnOuterClass.UrlResponse.newBuilder()
                    .setUrl(shortUrl)
                    .setOriginalUrl(url)
                    .setDate(newUrl.getDate().toString())
                    .build();

            responseObserver.onNext(urlResponse);

        }
        responseObserver.onCompleted();

    }

}