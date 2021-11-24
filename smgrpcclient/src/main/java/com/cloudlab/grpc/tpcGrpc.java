package com.cloudlab.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: tpc.proto")
public final class tpcGrpc {

  private tpcGrpc() {}

  public static final String SERVICE_NAME = "tpc";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.cloudlab.grpc.Tpc.ConnectionRequest,
      com.cloudlab.grpc.Tpc.ConnectionResponse> getGreetingServiceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GreetingService",
      requestType = com.cloudlab.grpc.Tpc.ConnectionRequest.class,
      responseType = com.cloudlab.grpc.Tpc.ConnectionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.cloudlab.grpc.Tpc.ConnectionRequest,
      com.cloudlab.grpc.Tpc.ConnectionResponse> getGreetingServiceMethod() {
    io.grpc.MethodDescriptor<com.cloudlab.grpc.Tpc.ConnectionRequest, com.cloudlab.grpc.Tpc.ConnectionResponse> getGreetingServiceMethod;
    if ((getGreetingServiceMethod = tpcGrpc.getGreetingServiceMethod) == null) {
      synchronized (tpcGrpc.class) {
        if ((getGreetingServiceMethod = tpcGrpc.getGreetingServiceMethod) == null) {
          tpcGrpc.getGreetingServiceMethod = getGreetingServiceMethod = 
              io.grpc.MethodDescriptor.<com.cloudlab.grpc.Tpc.ConnectionRequest, com.cloudlab.grpc.Tpc.ConnectionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "tpc", "GreetingService"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cloudlab.grpc.Tpc.ConnectionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cloudlab.grpc.Tpc.ConnectionResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new tpcMethodDescriptorSupplier("GreetingService"))
                  .build();
          }
        }
     }
     return getGreetingServiceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.cloudlab.grpc.Tpc.AllocationRequest,
      com.cloudlab.grpc.Tpc.AllocationResponse> getAllocationServiceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AllocationService",
      requestType = com.cloudlab.grpc.Tpc.AllocationRequest.class,
      responseType = com.cloudlab.grpc.Tpc.AllocationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.cloudlab.grpc.Tpc.AllocationRequest,
      com.cloudlab.grpc.Tpc.AllocationResponse> getAllocationServiceMethod() {
    io.grpc.MethodDescriptor<com.cloudlab.grpc.Tpc.AllocationRequest, com.cloudlab.grpc.Tpc.AllocationResponse> getAllocationServiceMethod;
    if ((getAllocationServiceMethod = tpcGrpc.getAllocationServiceMethod) == null) {
      synchronized (tpcGrpc.class) {
        if ((getAllocationServiceMethod = tpcGrpc.getAllocationServiceMethod) == null) {
          tpcGrpc.getAllocationServiceMethod = getAllocationServiceMethod = 
              io.grpc.MethodDescriptor.<com.cloudlab.grpc.Tpc.AllocationRequest, com.cloudlab.grpc.Tpc.AllocationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "tpc", "AllocationService"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cloudlab.grpc.Tpc.AllocationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cloudlab.grpc.Tpc.AllocationResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new tpcMethodDescriptorSupplier("AllocationService"))
                  .build();
          }
        }
     }
     return getAllocationServiceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.cloudlab.grpc.Tpc.NotificationMessage,
      com.cloudlab.grpc.Tpc.Empty> getNotifyingServiceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "NotifyingService",
      requestType = com.cloudlab.grpc.Tpc.NotificationMessage.class,
      responseType = com.cloudlab.grpc.Tpc.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.cloudlab.grpc.Tpc.NotificationMessage,
      com.cloudlab.grpc.Tpc.Empty> getNotifyingServiceMethod() {
    io.grpc.MethodDescriptor<com.cloudlab.grpc.Tpc.NotificationMessage, com.cloudlab.grpc.Tpc.Empty> getNotifyingServiceMethod;
    if ((getNotifyingServiceMethod = tpcGrpc.getNotifyingServiceMethod) == null) {
      synchronized (tpcGrpc.class) {
        if ((getNotifyingServiceMethod = tpcGrpc.getNotifyingServiceMethod) == null) {
          tpcGrpc.getNotifyingServiceMethod = getNotifyingServiceMethod = 
              io.grpc.MethodDescriptor.<com.cloudlab.grpc.Tpc.NotificationMessage, com.cloudlab.grpc.Tpc.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "tpc", "NotifyingService"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cloudlab.grpc.Tpc.NotificationMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cloudlab.grpc.Tpc.Empty.getDefaultInstance()))
                  .setSchemaDescriptor(new tpcMethodDescriptorSupplier("NotifyingService"))
                  .build();
          }
        }
     }
     return getNotifyingServiceMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static tpcStub newStub(io.grpc.Channel channel) {
    return new tpcStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static tpcBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new tpcBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static tpcFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new tpcFutureStub(channel);
  }

  /**
   */
  public static abstract class tpcImplBase implements io.grpc.BindableService {

    /**
     */
    public void greetingService(com.cloudlab.grpc.Tpc.ConnectionRequest request,
        io.grpc.stub.StreamObserver<com.cloudlab.grpc.Tpc.ConnectionResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGreetingServiceMethod(), responseObserver);
    }

    /**
     */
    public void allocationService(com.cloudlab.grpc.Tpc.AllocationRequest request,
        io.grpc.stub.StreamObserver<com.cloudlab.grpc.Tpc.AllocationResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAllocationServiceMethod(), responseObserver);
    }

    /**
     */
    public void notifyingService(com.cloudlab.grpc.Tpc.NotificationMessage request,
        io.grpc.stub.StreamObserver<com.cloudlab.grpc.Tpc.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getNotifyingServiceMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGreetingServiceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.cloudlab.grpc.Tpc.ConnectionRequest,
                com.cloudlab.grpc.Tpc.ConnectionResponse>(
                  this, METHODID_GREETING_SERVICE)))
          .addMethod(
            getAllocationServiceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.cloudlab.grpc.Tpc.AllocationRequest,
                com.cloudlab.grpc.Tpc.AllocationResponse>(
                  this, METHODID_ALLOCATION_SERVICE)))
          .addMethod(
            getNotifyingServiceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.cloudlab.grpc.Tpc.NotificationMessage,
                com.cloudlab.grpc.Tpc.Empty>(
                  this, METHODID_NOTIFYING_SERVICE)))
          .build();
    }
  }

  /**
   */
  public static final class tpcStub extends io.grpc.stub.AbstractStub<tpcStub> {
    private tpcStub(io.grpc.Channel channel) {
      super(channel);
    }

    private tpcStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected tpcStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new tpcStub(channel, callOptions);
    }

    /**
     */
    public void greetingService(com.cloudlab.grpc.Tpc.ConnectionRequest request,
        io.grpc.stub.StreamObserver<com.cloudlab.grpc.Tpc.ConnectionResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGreetingServiceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void allocationService(com.cloudlab.grpc.Tpc.AllocationRequest request,
        io.grpc.stub.StreamObserver<com.cloudlab.grpc.Tpc.AllocationResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAllocationServiceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void notifyingService(com.cloudlab.grpc.Tpc.NotificationMessage request,
        io.grpc.stub.StreamObserver<com.cloudlab.grpc.Tpc.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNotifyingServiceMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class tpcBlockingStub extends io.grpc.stub.AbstractStub<tpcBlockingStub> {
    private tpcBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private tpcBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected tpcBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new tpcBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.cloudlab.grpc.Tpc.ConnectionResponse greetingService(com.cloudlab.grpc.Tpc.ConnectionRequest request) {
      return blockingUnaryCall(
          getChannel(), getGreetingServiceMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.cloudlab.grpc.Tpc.AllocationResponse allocationService(com.cloudlab.grpc.Tpc.AllocationRequest request) {
      return blockingUnaryCall(
          getChannel(), getAllocationServiceMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.cloudlab.grpc.Tpc.Empty notifyingService(com.cloudlab.grpc.Tpc.NotificationMessage request) {
      return blockingUnaryCall(
          getChannel(), getNotifyingServiceMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class tpcFutureStub extends io.grpc.stub.AbstractStub<tpcFutureStub> {
    private tpcFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private tpcFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected tpcFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new tpcFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cloudlab.grpc.Tpc.ConnectionResponse> greetingService(
        com.cloudlab.grpc.Tpc.ConnectionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGreetingServiceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cloudlab.grpc.Tpc.AllocationResponse> allocationService(
        com.cloudlab.grpc.Tpc.AllocationRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAllocationServiceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cloudlab.grpc.Tpc.Empty> notifyingService(
        com.cloudlab.grpc.Tpc.NotificationMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getNotifyingServiceMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GREETING_SERVICE = 0;
  private static final int METHODID_ALLOCATION_SERVICE = 1;
  private static final int METHODID_NOTIFYING_SERVICE = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final tpcImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(tpcImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GREETING_SERVICE:
          serviceImpl.greetingService((com.cloudlab.grpc.Tpc.ConnectionRequest) request,
              (io.grpc.stub.StreamObserver<com.cloudlab.grpc.Tpc.ConnectionResponse>) responseObserver);
          break;
        case METHODID_ALLOCATION_SERVICE:
          serviceImpl.allocationService((com.cloudlab.grpc.Tpc.AllocationRequest) request,
              (io.grpc.stub.StreamObserver<com.cloudlab.grpc.Tpc.AllocationResponse>) responseObserver);
          break;
        case METHODID_NOTIFYING_SERVICE:
          serviceImpl.notifyingService((com.cloudlab.grpc.Tpc.NotificationMessage) request,
              (io.grpc.stub.StreamObserver<com.cloudlab.grpc.Tpc.Empty>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class tpcBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    tpcBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.cloudlab.grpc.Tpc.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("tpc");
    }
  }

  private static final class tpcFileDescriptorSupplier
      extends tpcBaseDescriptorSupplier {
    tpcFileDescriptorSupplier() {}
  }

  private static final class tpcMethodDescriptorSupplier
      extends tpcBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    tpcMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (tpcGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new tpcFileDescriptorSupplier())
              .addMethod(getGreetingServiceMethod())
              .addMethod(getAllocationServiceMethod())
              .addMethod(getNotifyingServiceMethod())
              .build();
        }
      }
    }
    return result;
  }
}
