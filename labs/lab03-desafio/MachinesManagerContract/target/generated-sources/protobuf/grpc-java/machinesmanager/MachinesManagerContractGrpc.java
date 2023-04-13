package machinesmanager;

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
    comments = "Source: MachinesManager.proto")
public final class MachinesManagerContractGrpc {

  private MachinesManagerContractGrpc() {}

  public static final String SERVICE_NAME = "machinesmanager.MachinesManagerContract";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<machinesmanager.Information,
      machinesmanager.Information> getConnectToManagerMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "connectToManager",
      requestType = machinesmanager.Information.class,
      responseType = machinesmanager.Information.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<machinesmanager.Information,
      machinesmanager.Information> getConnectToManagerMethod() {
    io.grpc.MethodDescriptor<machinesmanager.Information, machinesmanager.Information> getConnectToManagerMethod;
    if ((getConnectToManagerMethod = MachinesManagerContractGrpc.getConnectToManagerMethod) == null) {
      synchronized (MachinesManagerContractGrpc.class) {
        if ((getConnectToManagerMethod = MachinesManagerContractGrpc.getConnectToManagerMethod) == null) {
          MachinesManagerContractGrpc.getConnectToManagerMethod = getConnectToManagerMethod =
              io.grpc.MethodDescriptor.<machinesmanager.Information, machinesmanager.Information>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "connectToManager"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  machinesmanager.Information.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  machinesmanager.Information.getDefaultInstance()))
              .setSchemaDescriptor(new MachinesManagerContractMethodDescriptorSupplier("connectToManager"))
              .build();
        }
      }
    }
    return getConnectToManagerMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MachinesManagerContractStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MachinesManagerContractStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MachinesManagerContractStub>() {
        @java.lang.Override
        public MachinesManagerContractStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MachinesManagerContractStub(channel, callOptions);
        }
      };
    return MachinesManagerContractStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MachinesManagerContractBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MachinesManagerContractBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MachinesManagerContractBlockingStub>() {
        @java.lang.Override
        public MachinesManagerContractBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MachinesManagerContractBlockingStub(channel, callOptions);
        }
      };
    return MachinesManagerContractBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MachinesManagerContractFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MachinesManagerContractFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MachinesManagerContractFutureStub>() {
        @java.lang.Override
        public MachinesManagerContractFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MachinesManagerContractFutureStub(channel, callOptions);
        }
      };
    return MachinesManagerContractFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class MachinesManagerContractImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<machinesmanager.Information> connectToManager(
        io.grpc.stub.StreamObserver<machinesmanager.Information> responseObserver) {
      return asyncUnimplementedStreamingCall(getConnectToManagerMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getConnectToManagerMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                machinesmanager.Information,
                machinesmanager.Information>(
                  this, METHODID_CONNECT_TO_MANAGER)))
          .build();
    }
  }

  /**
   */
  public static final class MachinesManagerContractStub extends io.grpc.stub.AbstractAsyncStub<MachinesManagerContractStub> {
    private MachinesManagerContractStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MachinesManagerContractStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MachinesManagerContractStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<machinesmanager.Information> connectToManager(
        io.grpc.stub.StreamObserver<machinesmanager.Information> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getConnectToManagerMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class MachinesManagerContractBlockingStub extends io.grpc.stub.AbstractBlockingStub<MachinesManagerContractBlockingStub> {
    private MachinesManagerContractBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MachinesManagerContractBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MachinesManagerContractBlockingStub(channel, callOptions);
    }
  }

  /**
   */
  public static final class MachinesManagerContractFutureStub extends io.grpc.stub.AbstractFutureStub<MachinesManagerContractFutureStub> {
    private MachinesManagerContractFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MachinesManagerContractFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MachinesManagerContractFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_CONNECT_TO_MANAGER = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MachinesManagerContractImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MachinesManagerContractImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CONNECT_TO_MANAGER:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.connectToManager(
              (io.grpc.stub.StreamObserver<machinesmanager.Information>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class MachinesManagerContractBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MachinesManagerContractBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return machinesmanager.MachinesManager.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MachinesManagerContract");
    }
  }

  private static final class MachinesManagerContractFileDescriptorSupplier
      extends MachinesManagerContractBaseDescriptorSupplier {
    MachinesManagerContractFileDescriptorSupplier() {}
  }

  private static final class MachinesManagerContractMethodDescriptorSupplier
      extends MachinesManagerContractBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MachinesManagerContractMethodDescriptorSupplier(String methodName) {
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
      synchronized (MachinesManagerContractGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MachinesManagerContractFileDescriptorSupplier())
              .addMethod(getConnectToManagerMethod())
              .build();
        }
      }
    }
    return result;
  }
}
