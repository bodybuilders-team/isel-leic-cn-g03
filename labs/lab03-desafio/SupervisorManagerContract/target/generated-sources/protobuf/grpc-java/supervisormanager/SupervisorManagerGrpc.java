package supervisormanager;

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
    value = "by gRPC proto compiler (version 1.28.1)",
    comments = "Source: SupervisorManager.proto")
public final class SupervisorManagerGrpc {

  private SupervisorManagerGrpc() {}

  public static final String SERVICE_NAME = "supervisormanager.SupervisorManager";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<supervisormanager.Void,
      supervisormanager.AllMachineIDs> getGetMachinesIdsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getMachinesIds",
      requestType = supervisormanager.Void.class,
      responseType = supervisormanager.AllMachineIDs.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<supervisormanager.Void,
      supervisormanager.AllMachineIDs> getGetMachinesIdsMethod() {
    io.grpc.MethodDescriptor<supervisormanager.Void, supervisormanager.AllMachineIDs> getGetMachinesIdsMethod;
    if ((getGetMachinesIdsMethod = SupervisorManagerGrpc.getGetMachinesIdsMethod) == null) {
      synchronized (SupervisorManagerGrpc.class) {
        if ((getGetMachinesIdsMethod = SupervisorManagerGrpc.getGetMachinesIdsMethod) == null) {
          SupervisorManagerGrpc.getGetMachinesIdsMethod = getGetMachinesIdsMethod =
              io.grpc.MethodDescriptor.<supervisormanager.Void, supervisormanager.AllMachineIDs>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getMachinesIds"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  supervisormanager.Void.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  supervisormanager.AllMachineIDs.getDefaultInstance()))
              .setSchemaDescriptor(new SupervisorManagerMethodDescriptorSupplier("getMachinesIds"))
              .build();
        }
      }
    }
    return getGetMachinesIdsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<supervisormanager.Command,
      supervisormanager.Void> getSendCommandToMachineMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sendCommandToMachine",
      requestType = supervisormanager.Command.class,
      responseType = supervisormanager.Void.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<supervisormanager.Command,
      supervisormanager.Void> getSendCommandToMachineMethod() {
    io.grpc.MethodDescriptor<supervisormanager.Command, supervisormanager.Void> getSendCommandToMachineMethod;
    if ((getSendCommandToMachineMethod = SupervisorManagerGrpc.getSendCommandToMachineMethod) == null) {
      synchronized (SupervisorManagerGrpc.class) {
        if ((getSendCommandToMachineMethod = SupervisorManagerGrpc.getSendCommandToMachineMethod) == null) {
          SupervisorManagerGrpc.getSendCommandToMachineMethod = getSendCommandToMachineMethod =
              io.grpc.MethodDescriptor.<supervisormanager.Command, supervisormanager.Void>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "sendCommandToMachine"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  supervisormanager.Command.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  supervisormanager.Void.getDefaultInstance()))
              .setSchemaDescriptor(new SupervisorManagerMethodDescriptorSupplier("sendCommandToMachine"))
              .build();
        }
      }
    }
    return getSendCommandToMachineMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SupervisorManagerStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SupervisorManagerStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SupervisorManagerStub>() {
        @java.lang.Override
        public SupervisorManagerStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SupervisorManagerStub(channel, callOptions);
        }
      };
    return SupervisorManagerStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SupervisorManagerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SupervisorManagerBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SupervisorManagerBlockingStub>() {
        @java.lang.Override
        public SupervisorManagerBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SupervisorManagerBlockingStub(channel, callOptions);
        }
      };
    return SupervisorManagerBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SupervisorManagerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SupervisorManagerFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SupervisorManagerFutureStub>() {
        @java.lang.Override
        public SupervisorManagerFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SupervisorManagerFutureStub(channel, callOptions);
        }
      };
    return SupervisorManagerFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class SupervisorManagerImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * obter os Ids de todas as máquinas
     * </pre>
     */
    public void getMachinesIds(supervisormanager.Void request,
        io.grpc.stub.StreamObserver<supervisormanager.AllMachineIDs> responseObserver) {
      asyncUnimplementedUnaryCall(getGetMachinesIdsMethod(), responseObserver);
    }

    /**
     */
    public void sendCommandToMachine(supervisormanager.Command request,
        io.grpc.stub.StreamObserver<supervisormanager.Void> responseObserver) {
      asyncUnimplementedUnaryCall(getSendCommandToMachineMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetMachinesIdsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                supervisormanager.Void,
                supervisormanager.AllMachineIDs>(
                  this, METHODID_GET_MACHINES_IDS)))
          .addMethod(
            getSendCommandToMachineMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                supervisormanager.Command,
                supervisormanager.Void>(
                  this, METHODID_SEND_COMMAND_TO_MACHINE)))
          .build();
    }
  }

  /**
   */
  public static final class SupervisorManagerStub extends io.grpc.stub.AbstractAsyncStub<SupervisorManagerStub> {
    private SupervisorManagerStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SupervisorManagerStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SupervisorManagerStub(channel, callOptions);
    }

    /**
     * <pre>
     * obter os Ids de todas as máquinas
     * </pre>
     */
    public void getMachinesIds(supervisormanager.Void request,
        io.grpc.stub.StreamObserver<supervisormanager.AllMachineIDs> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetMachinesIdsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendCommandToMachine(supervisormanager.Command request,
        io.grpc.stub.StreamObserver<supervisormanager.Void> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendCommandToMachineMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class SupervisorManagerBlockingStub extends io.grpc.stub.AbstractBlockingStub<SupervisorManagerBlockingStub> {
    private SupervisorManagerBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SupervisorManagerBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SupervisorManagerBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * obter os Ids de todas as máquinas
     * </pre>
     */
    public supervisormanager.AllMachineIDs getMachinesIds(supervisormanager.Void request) {
      return blockingUnaryCall(
          getChannel(), getGetMachinesIdsMethod(), getCallOptions(), request);
    }

    /**
     */
    public supervisormanager.Void sendCommandToMachine(supervisormanager.Command request) {
      return blockingUnaryCall(
          getChannel(), getSendCommandToMachineMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class SupervisorManagerFutureStub extends io.grpc.stub.AbstractFutureStub<SupervisorManagerFutureStub> {
    private SupervisorManagerFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SupervisorManagerFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SupervisorManagerFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * obter os Ids de todas as máquinas
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<supervisormanager.AllMachineIDs> getMachinesIds(
        supervisormanager.Void request) {
      return futureUnaryCall(
          getChannel().newCall(getGetMachinesIdsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<supervisormanager.Void> sendCommandToMachine(
        supervisormanager.Command request) {
      return futureUnaryCall(
          getChannel().newCall(getSendCommandToMachineMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_MACHINES_IDS = 0;
  private static final int METHODID_SEND_COMMAND_TO_MACHINE = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final SupervisorManagerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(SupervisorManagerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_MACHINES_IDS:
          serviceImpl.getMachinesIds((supervisormanager.Void) request,
              (io.grpc.stub.StreamObserver<supervisormanager.AllMachineIDs>) responseObserver);
          break;
        case METHODID_SEND_COMMAND_TO_MACHINE:
          serviceImpl.sendCommandToMachine((supervisormanager.Command) request,
              (io.grpc.stub.StreamObserver<supervisormanager.Void>) responseObserver);
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

  private static abstract class SupervisorManagerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SupervisorManagerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return supervisormanager.SupervisorManagerOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SupervisorManager");
    }
  }

  private static final class SupervisorManagerFileDescriptorSupplier
      extends SupervisorManagerBaseDescriptorSupplier {
    SupervisorManagerFileDescriptorSupplier() {}
  }

  private static final class SupervisorManagerMethodDescriptorSupplier
      extends SupervisorManagerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    SupervisorManagerMethodDescriptorSupplier(String methodName) {
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
      synchronized (SupervisorManagerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SupervisorManagerFileDescriptorSupplier())
              .addMethod(getGetMachinesIdsMethod())
              .addMethod(getSendCommandToMachineMethod())
              .build();
        }
      }
    }
    return result;
  }
}
