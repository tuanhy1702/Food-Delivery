package org.example;


import GRPC.*;
import io.grpc.*;

public class grbc2 {
    public static void main(String[] args) {
        String studentCode ="B22DCCN418";
        String questionAlias ="HRxOJhoc";
        ManagedChannel channel = ManagedChannelBuilder.forAddress("36.50.135.242", 2240).usePlaintext().build();
        JudgeServiceGrpc.JudgeServiceBlockingStub stub = JudgeServiceGrpc.newBlockingStub(channel);

        JudgeRequest request = JudgeRequest.newBuilder().setQuestionAlias(questionAlias).setStudentCode(studentCode).build();
        JudgeResponse response = stub.request(request);
        System.out.println(response.getRequestId()+" "+response.getData());
        String [] a = response.getData().split(",");
        long sum =0;
        for(String x: a){
            sum+=Integer.parseInt(x);
        }
        SubmitRequest submitRequest = SubmitRequest.newBuilder().setStudentCode(studentCode)
                .setQuestionAlias(questionAlias)
                .setRequestId(response.getRequestId())
                .setAnswer(String.valueOf(sum))
                .build();
        SubmitResponse st = stub.submit(submitRequest);
        System.out.println(st);

    }
}