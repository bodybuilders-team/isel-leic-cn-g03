// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: SupervisorManager.proto

package supervisormanager;

/**
 * Protobuf type {@code supervisormanager.Command}
 */
public  final class Command extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:supervisormanager.Command)
    CommandOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Command.newBuilder() to construct.
  private Command(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Command() {
    ctltext_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new Command();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private Command(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            supervisormanager.MachID.Builder subBuilder = null;
            if (id_ != null) {
              subBuilder = id_.toBuilder();
            }
            id_ = input.readMessage(supervisormanager.MachID.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(id_);
              id_ = subBuilder.buildPartial();
            }

            break;
          }
          case 16: {

            ctlNumber_ = input.readInt32();
            break;
          }
          case 26: {
            java.lang.String s = input.readStringRequireUtf8();

            ctltext_ = s;
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return supervisormanager.SupervisorManagerOuterClass.internal_static_supervisormanager_Command_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return supervisormanager.SupervisorManagerOuterClass.internal_static_supervisormanager_Command_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            supervisormanager.Command.class, supervisormanager.Command.Builder.class);
  }

  public static final int ID_FIELD_NUMBER = 1;
  private supervisormanager.MachID id_;
  /**
   * <code>.supervisormanager.MachID id = 1;</code>
   * @return Whether the id field is set.
   */
  public boolean hasId() {
    return id_ != null;
  }
  /**
   * <code>.supervisormanager.MachID id = 1;</code>
   * @return The id.
   */
  public supervisormanager.MachID getId() {
    return id_ == null ? supervisormanager.MachID.getDefaultInstance() : id_;
  }
  /**
   * <code>.supervisormanager.MachID id = 1;</code>
   */
  public supervisormanager.MachIDOrBuilder getIdOrBuilder() {
    return getId();
  }

  public static final int CTLNUMBER_FIELD_NUMBER = 2;
  private int ctlNumber_;
  /**
   * <code>int32 ctlNumber = 2;</code>
   * @return The ctlNumber.
   */
  public int getCtlNumber() {
    return ctlNumber_;
  }

  public static final int CTLTEXT_FIELD_NUMBER = 3;
  private volatile java.lang.Object ctltext_;
  /**
   * <code>string ctltext = 3;</code>
   * @return The ctltext.
   */
  public java.lang.String getCtltext() {
    java.lang.Object ref = ctltext_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      ctltext_ = s;
      return s;
    }
  }
  /**
   * <code>string ctltext = 3;</code>
   * @return The bytes for ctltext.
   */
  public com.google.protobuf.ByteString
      getCtltextBytes() {
    java.lang.Object ref = ctltext_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      ctltext_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (id_ != null) {
      output.writeMessage(1, getId());
    }
    if (ctlNumber_ != 0) {
      output.writeInt32(2, ctlNumber_);
    }
    if (!getCtltextBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, ctltext_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (id_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getId());
    }
    if (ctlNumber_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, ctlNumber_);
    }
    if (!getCtltextBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, ctltext_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof supervisormanager.Command)) {
      return super.equals(obj);
    }
    supervisormanager.Command other = (supervisormanager.Command) obj;

    if (hasId() != other.hasId()) return false;
    if (hasId()) {
      if (!getId()
          .equals(other.getId())) return false;
    }
    if (getCtlNumber()
        != other.getCtlNumber()) return false;
    if (!getCtltext()
        .equals(other.getCtltext())) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasId()) {
      hash = (37 * hash) + ID_FIELD_NUMBER;
      hash = (53 * hash) + getId().hashCode();
    }
    hash = (37 * hash) + CTLNUMBER_FIELD_NUMBER;
    hash = (53 * hash) + getCtlNumber();
    hash = (37 * hash) + CTLTEXT_FIELD_NUMBER;
    hash = (53 * hash) + getCtltext().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static supervisormanager.Command parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static supervisormanager.Command parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static supervisormanager.Command parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static supervisormanager.Command parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static supervisormanager.Command parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static supervisormanager.Command parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static supervisormanager.Command parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static supervisormanager.Command parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static supervisormanager.Command parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static supervisormanager.Command parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static supervisormanager.Command parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static supervisormanager.Command parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(supervisormanager.Command prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code supervisormanager.Command}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:supervisormanager.Command)
      supervisormanager.CommandOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return supervisormanager.SupervisorManagerOuterClass.internal_static_supervisormanager_Command_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return supervisormanager.SupervisorManagerOuterClass.internal_static_supervisormanager_Command_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              supervisormanager.Command.class, supervisormanager.Command.Builder.class);
    }

    // Construct using supervisormanager.Command.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (idBuilder_ == null) {
        id_ = null;
      } else {
        id_ = null;
        idBuilder_ = null;
      }
      ctlNumber_ = 0;

      ctltext_ = "";

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return supervisormanager.SupervisorManagerOuterClass.internal_static_supervisormanager_Command_descriptor;
    }

    @java.lang.Override
    public supervisormanager.Command getDefaultInstanceForType() {
      return supervisormanager.Command.getDefaultInstance();
    }

    @java.lang.Override
    public supervisormanager.Command build() {
      supervisormanager.Command result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public supervisormanager.Command buildPartial() {
      supervisormanager.Command result = new supervisormanager.Command(this);
      if (idBuilder_ == null) {
        result.id_ = id_;
      } else {
        result.id_ = idBuilder_.build();
      }
      result.ctlNumber_ = ctlNumber_;
      result.ctltext_ = ctltext_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof supervisormanager.Command) {
        return mergeFrom((supervisormanager.Command)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(supervisormanager.Command other) {
      if (other == supervisormanager.Command.getDefaultInstance()) return this;
      if (other.hasId()) {
        mergeId(other.getId());
      }
      if (other.getCtlNumber() != 0) {
        setCtlNumber(other.getCtlNumber());
      }
      if (!other.getCtltext().isEmpty()) {
        ctltext_ = other.ctltext_;
        onChanged();
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      supervisormanager.Command parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (supervisormanager.Command) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private supervisormanager.MachID id_;
    private com.google.protobuf.SingleFieldBuilderV3<
        supervisormanager.MachID, supervisormanager.MachID.Builder, supervisormanager.MachIDOrBuilder> idBuilder_;
    /**
     * <code>.supervisormanager.MachID id = 1;</code>
     * @return Whether the id field is set.
     */
    public boolean hasId() {
      return idBuilder_ != null || id_ != null;
    }
    /**
     * <code>.supervisormanager.MachID id = 1;</code>
     * @return The id.
     */
    public supervisormanager.MachID getId() {
      if (idBuilder_ == null) {
        return id_ == null ? supervisormanager.MachID.getDefaultInstance() : id_;
      } else {
        return idBuilder_.getMessage();
      }
    }
    /**
     * <code>.supervisormanager.MachID id = 1;</code>
     */
    public Builder setId(supervisormanager.MachID value) {
      if (idBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        id_ = value;
        onChanged();
      } else {
        idBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.supervisormanager.MachID id = 1;</code>
     */
    public Builder setId(
        supervisormanager.MachID.Builder builderForValue) {
      if (idBuilder_ == null) {
        id_ = builderForValue.build();
        onChanged();
      } else {
        idBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.supervisormanager.MachID id = 1;</code>
     */
    public Builder mergeId(supervisormanager.MachID value) {
      if (idBuilder_ == null) {
        if (id_ != null) {
          id_ =
            supervisormanager.MachID.newBuilder(id_).mergeFrom(value).buildPartial();
        } else {
          id_ = value;
        }
        onChanged();
      } else {
        idBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.supervisormanager.MachID id = 1;</code>
     */
    public Builder clearId() {
      if (idBuilder_ == null) {
        id_ = null;
        onChanged();
      } else {
        id_ = null;
        idBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.supervisormanager.MachID id = 1;</code>
     */
    public supervisormanager.MachID.Builder getIdBuilder() {
      
      onChanged();
      return getIdFieldBuilder().getBuilder();
    }
    /**
     * <code>.supervisormanager.MachID id = 1;</code>
     */
    public supervisormanager.MachIDOrBuilder getIdOrBuilder() {
      if (idBuilder_ != null) {
        return idBuilder_.getMessageOrBuilder();
      } else {
        return id_ == null ?
            supervisormanager.MachID.getDefaultInstance() : id_;
      }
    }
    /**
     * <code>.supervisormanager.MachID id = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        supervisormanager.MachID, supervisormanager.MachID.Builder, supervisormanager.MachIDOrBuilder> 
        getIdFieldBuilder() {
      if (idBuilder_ == null) {
        idBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            supervisormanager.MachID, supervisormanager.MachID.Builder, supervisormanager.MachIDOrBuilder>(
                getId(),
                getParentForChildren(),
                isClean());
        id_ = null;
      }
      return idBuilder_;
    }

    private int ctlNumber_ ;
    /**
     * <code>int32 ctlNumber = 2;</code>
     * @return The ctlNumber.
     */
    public int getCtlNumber() {
      return ctlNumber_;
    }
    /**
     * <code>int32 ctlNumber = 2;</code>
     * @param value The ctlNumber to set.
     * @return This builder for chaining.
     */
    public Builder setCtlNumber(int value) {
      
      ctlNumber_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 ctlNumber = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearCtlNumber() {
      
      ctlNumber_ = 0;
      onChanged();
      return this;
    }

    private java.lang.Object ctltext_ = "";
    /**
     * <code>string ctltext = 3;</code>
     * @return The ctltext.
     */
    public java.lang.String getCtltext() {
      java.lang.Object ref = ctltext_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        ctltext_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string ctltext = 3;</code>
     * @return The bytes for ctltext.
     */
    public com.google.protobuf.ByteString
        getCtltextBytes() {
      java.lang.Object ref = ctltext_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        ctltext_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string ctltext = 3;</code>
     * @param value The ctltext to set.
     * @return This builder for chaining.
     */
    public Builder setCtltext(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      ctltext_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string ctltext = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearCtltext() {
      
      ctltext_ = getDefaultInstance().getCtltext();
      onChanged();
      return this;
    }
    /**
     * <code>string ctltext = 3;</code>
     * @param value The bytes for ctltext to set.
     * @return This builder for chaining.
     */
    public Builder setCtltextBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      ctltext_ = value;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:supervisormanager.Command)
  }

  // @@protoc_insertion_point(class_scope:supervisormanager.Command)
  private static final supervisormanager.Command DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new supervisormanager.Command();
  }

  public static supervisormanager.Command getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Command>
      PARSER = new com.google.protobuf.AbstractParser<Command>() {
    @java.lang.Override
    public Command parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new Command(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Command> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Command> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public supervisormanager.Command getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

