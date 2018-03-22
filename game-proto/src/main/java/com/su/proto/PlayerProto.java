// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: proto/Player.proto

package com.su.proto;

public final class PlayerProto {
  private PlayerProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface PlayerDataProOrBuilder extends
      // @@protoc_insertion_point(interface_extends:com.su.proto.PlayerDataPro)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * id
     * </pre>
     *
     * <code>optional int64 id = 1 [default = -1];</code>
     */
    boolean hasId();
    /**
     * <pre>
     * id
     * </pre>
     *
     * <code>optional int64 id = 1 [default = -1];</code>
     */
    long getId();

    /**
     * <pre>
     * 名字
     * </pre>
     *
     * <code>optional string name = 2;</code>
     */
    boolean hasName();
    /**
     * <pre>
     * 名字
     * </pre>
     *
     * <code>optional string name = 2;</code>
     */
    java.lang.String getName();
    /**
     * <pre>
     * 名字
     * </pre>
     *
     * <code>optional string name = 2;</code>
     */
    com.google.protobuf.ByteString
        getNameBytes();

    /**
     * <pre>
     * 头像
     * </pre>
     *
     * <code>optional string picture = 3;</code>
     */
    boolean hasPicture();
    /**
     * <pre>
     * 头像
     * </pre>
     *
     * <code>optional string picture = 3;</code>
     */
    java.lang.String getPicture();
    /**
     * <pre>
     * 头像
     * </pre>
     *
     * <code>optional string picture = 3;</code>
     */
    com.google.protobuf.ByteString
        getPictureBytes();

    /**
     * <pre>
     * 等级
     * </pre>
     *
     * <code>optional int32 level = 4 [default = -1];</code>
     */
    boolean hasLevel();
    /**
     * <pre>
     * 等级
     * </pre>
     *
     * <code>optional int32 level = 4 [default = -1];</code>
     */
    int getLevel();

    /**
     * <pre>
     * vip等级
     * </pre>
     *
     * <code>optional int32 vipLevel = 5 [default = -1];</code>
     */
    boolean hasVipLevel();
    /**
     * <pre>
     * vip等级
     * </pre>
     *
     * <code>optional int32 vipLevel = 5 [default = -1];</code>
     */
    int getVipLevel();
  }
  /**
   * <pre>
   * 玩家数据
   * </pre>
   *
   * Protobuf type {@code com.su.proto.PlayerDataPro}
   */
  public  static final class PlayerDataPro extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:com.su.proto.PlayerDataPro)
      PlayerDataProOrBuilder {
    // Use PlayerDataPro.newBuilder() to construct.
    private PlayerDataPro(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private PlayerDataPro() {
      id_ = -1L;
      name_ = "";
      picture_ = "";
      level_ = -1;
      vipLevel_ = -1;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private PlayerDataPro(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
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
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              bitField0_ |= 0x00000001;
              id_ = input.readInt64();
              break;
            }
            case 18: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000002;
              name_ = bs;
              break;
            }
            case 26: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000004;
              picture_ = bs;
              break;
            }
            case 32: {
              bitField0_ |= 0x00000008;
              level_ = input.readInt32();
              break;
            }
            case 40: {
              bitField0_ |= 0x00000010;
              vipLevel_ = input.readInt32();
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
      return com.su.proto.PlayerProto.internal_static_com_su_proto_PlayerDataPro_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.su.proto.PlayerProto.internal_static_com_su_proto_PlayerDataPro_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.su.proto.PlayerProto.PlayerDataPro.class, com.su.proto.PlayerProto.PlayerDataPro.Builder.class);
    }

    private int bitField0_;
    public static final int ID_FIELD_NUMBER = 1;
    private long id_;
    /**
     * <pre>
     * id
     * </pre>
     *
     * <code>optional int64 id = 1 [default = -1];</code>
     */
    public boolean hasId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <pre>
     * id
     * </pre>
     *
     * <code>optional int64 id = 1 [default = -1];</code>
     */
    public long getId() {
      return id_;
    }

    public static final int NAME_FIELD_NUMBER = 2;
    private volatile java.lang.Object name_;
    /**
     * <pre>
     * 名字
     * </pre>
     *
     * <code>optional string name = 2;</code>
     */
    public boolean hasName() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <pre>
     * 名字
     * </pre>
     *
     * <code>optional string name = 2;</code>
     */
    public java.lang.String getName() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          name_ = s;
        }
        return s;
      }
    }
    /**
     * <pre>
     * 名字
     * </pre>
     *
     * <code>optional string name = 2;</code>
     */
    public com.google.protobuf.ByteString
        getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int PICTURE_FIELD_NUMBER = 3;
    private volatile java.lang.Object picture_;
    /**
     * <pre>
     * 头像
     * </pre>
     *
     * <code>optional string picture = 3;</code>
     */
    public boolean hasPicture() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <pre>
     * 头像
     * </pre>
     *
     * <code>optional string picture = 3;</code>
     */
    public java.lang.String getPicture() {
      java.lang.Object ref = picture_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          picture_ = s;
        }
        return s;
      }
    }
    /**
     * <pre>
     * 头像
     * </pre>
     *
     * <code>optional string picture = 3;</code>
     */
    public com.google.protobuf.ByteString
        getPictureBytes() {
      java.lang.Object ref = picture_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        picture_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int LEVEL_FIELD_NUMBER = 4;
    private int level_;
    /**
     * <pre>
     * 等级
     * </pre>
     *
     * <code>optional int32 level = 4 [default = -1];</code>
     */
    public boolean hasLevel() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <pre>
     * 等级
     * </pre>
     *
     * <code>optional int32 level = 4 [default = -1];</code>
     */
    public int getLevel() {
      return level_;
    }

    public static final int VIPLEVEL_FIELD_NUMBER = 5;
    private int vipLevel_;
    /**
     * <pre>
     * vip等级
     * </pre>
     *
     * <code>optional int32 vipLevel = 5 [default = -1];</code>
     */
    public boolean hasVipLevel() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    /**
     * <pre>
     * vip等级
     * </pre>
     *
     * <code>optional int32 vipLevel = 5 [default = -1];</code>
     */
    public int getVipLevel() {
      return vipLevel_;
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeInt64(1, id_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 2, name_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 3, picture_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeInt32(4, level_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeInt32(5, vipLevel_);
      }
      unknownFields.writeTo(output);
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(1, id_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, name_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, picture_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(4, level_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(5, vipLevel_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof com.su.proto.PlayerProto.PlayerDataPro)) {
        return super.equals(obj);
      }
      com.su.proto.PlayerProto.PlayerDataPro other = (com.su.proto.PlayerProto.PlayerDataPro) obj;

      boolean result = true;
      result = result && (hasId() == other.hasId());
      if (hasId()) {
        result = result && (getId()
            == other.getId());
      }
      result = result && (hasName() == other.hasName());
      if (hasName()) {
        result = result && getName()
            .equals(other.getName());
      }
      result = result && (hasPicture() == other.hasPicture());
      if (hasPicture()) {
        result = result && getPicture()
            .equals(other.getPicture());
      }
      result = result && (hasLevel() == other.hasLevel());
      if (hasLevel()) {
        result = result && (getLevel()
            == other.getLevel());
      }
      result = result && (hasVipLevel() == other.hasVipLevel());
      if (hasVipLevel()) {
        result = result && (getVipLevel()
            == other.getVipLevel());
      }
      result = result && unknownFields.equals(other.unknownFields);
      return result;
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
        hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
            getId());
      }
      if (hasName()) {
        hash = (37 * hash) + NAME_FIELD_NUMBER;
        hash = (53 * hash) + getName().hashCode();
      }
      if (hasPicture()) {
        hash = (37 * hash) + PICTURE_FIELD_NUMBER;
        hash = (53 * hash) + getPicture().hashCode();
      }
      if (hasLevel()) {
        hash = (37 * hash) + LEVEL_FIELD_NUMBER;
        hash = (53 * hash) + getLevel();
      }
      if (hasVipLevel()) {
        hash = (37 * hash) + VIPLEVEL_FIELD_NUMBER;
        hash = (53 * hash) + getVipLevel();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.su.proto.PlayerProto.PlayerDataPro parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.su.proto.PlayerProto.PlayerDataPro parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.su.proto.PlayerProto.PlayerDataPro parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.su.proto.PlayerProto.PlayerDataPro parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.su.proto.PlayerProto.PlayerDataPro parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.su.proto.PlayerProto.PlayerDataPro parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.su.proto.PlayerProto.PlayerDataPro parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static com.su.proto.PlayerProto.PlayerDataPro parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.su.proto.PlayerProto.PlayerDataPro parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.su.proto.PlayerProto.PlayerDataPro parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.su.proto.PlayerProto.PlayerDataPro prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
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
     * <pre>
     * 玩家数据
     * </pre>
     *
     * Protobuf type {@code com.su.proto.PlayerDataPro}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:com.su.proto.PlayerDataPro)
        com.su.proto.PlayerProto.PlayerDataProOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.su.proto.PlayerProto.internal_static_com_su_proto_PlayerDataPro_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.su.proto.PlayerProto.internal_static_com_su_proto_PlayerDataPro_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.su.proto.PlayerProto.PlayerDataPro.class, com.su.proto.PlayerProto.PlayerDataPro.Builder.class);
      }

      // Construct using com.su.proto.PlayerProto.PlayerDataPro.newBuilder()
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
      public Builder clear() {
        super.clear();
        id_ = -1L;
        bitField0_ = (bitField0_ & ~0x00000001);
        name_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        picture_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        level_ = -1;
        bitField0_ = (bitField0_ & ~0x00000008);
        vipLevel_ = -1;
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.su.proto.PlayerProto.internal_static_com_su_proto_PlayerDataPro_descriptor;
      }

      public com.su.proto.PlayerProto.PlayerDataPro getDefaultInstanceForType() {
        return com.su.proto.PlayerProto.PlayerDataPro.getDefaultInstance();
      }

      public com.su.proto.PlayerProto.PlayerDataPro build() {
        com.su.proto.PlayerProto.PlayerDataPro result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.su.proto.PlayerProto.PlayerDataPro buildPartial() {
        com.su.proto.PlayerProto.PlayerDataPro result = new com.su.proto.PlayerProto.PlayerDataPro(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.id_ = id_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.name_ = name_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.picture_ = picture_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.level_ = level_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.vipLevel_ = vipLevel_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.su.proto.PlayerProto.PlayerDataPro) {
          return mergeFrom((com.su.proto.PlayerProto.PlayerDataPro)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.su.proto.PlayerProto.PlayerDataPro other) {
        if (other == com.su.proto.PlayerProto.PlayerDataPro.getDefaultInstance()) return this;
        if (other.hasId()) {
          setId(other.getId());
        }
        if (other.hasName()) {
          bitField0_ |= 0x00000002;
          name_ = other.name_;
          onChanged();
        }
        if (other.hasPicture()) {
          bitField0_ |= 0x00000004;
          picture_ = other.picture_;
          onChanged();
        }
        if (other.hasLevel()) {
          setLevel(other.getLevel());
        }
        if (other.hasVipLevel()) {
          setVipLevel(other.getVipLevel());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.su.proto.PlayerProto.PlayerDataPro parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.su.proto.PlayerProto.PlayerDataPro) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private long id_ = -1L;
      /**
       * <pre>
       * id
       * </pre>
       *
       * <code>optional int64 id = 1 [default = -1];</code>
       */
      public boolean hasId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <pre>
       * id
       * </pre>
       *
       * <code>optional int64 id = 1 [default = -1];</code>
       */
      public long getId() {
        return id_;
      }
      /**
       * <pre>
       * id
       * </pre>
       *
       * <code>optional int64 id = 1 [default = -1];</code>
       */
      public Builder setId(long value) {
        bitField0_ |= 0x00000001;
        id_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * id
       * </pre>
       *
       * <code>optional int64 id = 1 [default = -1];</code>
       */
      public Builder clearId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        id_ = -1L;
        onChanged();
        return this;
      }

      private java.lang.Object name_ = "";
      /**
       * <pre>
       * 名字
       * </pre>
       *
       * <code>optional string name = 2;</code>
       */
      public boolean hasName() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <pre>
       * 名字
       * </pre>
       *
       * <code>optional string name = 2;</code>
       */
      public java.lang.String getName() {
        java.lang.Object ref = name_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            name_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <pre>
       * 名字
       * </pre>
       *
       * <code>optional string name = 2;</code>
       */
      public com.google.protobuf.ByteString
          getNameBytes() {
        java.lang.Object ref = name_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          name_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       * 名字
       * </pre>
       *
       * <code>optional string name = 2;</code>
       */
      public Builder setName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        name_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 名字
       * </pre>
       *
       * <code>optional string name = 2;</code>
       */
      public Builder clearName() {
        bitField0_ = (bitField0_ & ~0x00000002);
        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 名字
       * </pre>
       *
       * <code>optional string name = 2;</code>
       */
      public Builder setNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        name_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object picture_ = "";
      /**
       * <pre>
       * 头像
       * </pre>
       *
       * <code>optional string picture = 3;</code>
       */
      public boolean hasPicture() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <pre>
       * 头像
       * </pre>
       *
       * <code>optional string picture = 3;</code>
       */
      public java.lang.String getPicture() {
        java.lang.Object ref = picture_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            picture_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <pre>
       * 头像
       * </pre>
       *
       * <code>optional string picture = 3;</code>
       */
      public com.google.protobuf.ByteString
          getPictureBytes() {
        java.lang.Object ref = picture_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          picture_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       * 头像
       * </pre>
       *
       * <code>optional string picture = 3;</code>
       */
      public Builder setPicture(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        picture_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 头像
       * </pre>
       *
       * <code>optional string picture = 3;</code>
       */
      public Builder clearPicture() {
        bitField0_ = (bitField0_ & ~0x00000004);
        picture_ = getDefaultInstance().getPicture();
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 头像
       * </pre>
       *
       * <code>optional string picture = 3;</code>
       */
      public Builder setPictureBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        picture_ = value;
        onChanged();
        return this;
      }

      private int level_ = -1;
      /**
       * <pre>
       * 等级
       * </pre>
       *
       * <code>optional int32 level = 4 [default = -1];</code>
       */
      public boolean hasLevel() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <pre>
       * 等级
       * </pre>
       *
       * <code>optional int32 level = 4 [default = -1];</code>
       */
      public int getLevel() {
        return level_;
      }
      /**
       * <pre>
       * 等级
       * </pre>
       *
       * <code>optional int32 level = 4 [default = -1];</code>
       */
      public Builder setLevel(int value) {
        bitField0_ |= 0x00000008;
        level_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 等级
       * </pre>
       *
       * <code>optional int32 level = 4 [default = -1];</code>
       */
      public Builder clearLevel() {
        bitField0_ = (bitField0_ & ~0x00000008);
        level_ = -1;
        onChanged();
        return this;
      }

      private int vipLevel_ = -1;
      /**
       * <pre>
       * vip等级
       * </pre>
       *
       * <code>optional int32 vipLevel = 5 [default = -1];</code>
       */
      public boolean hasVipLevel() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      /**
       * <pre>
       * vip等级
       * </pre>
       *
       * <code>optional int32 vipLevel = 5 [default = -1];</code>
       */
      public int getVipLevel() {
        return vipLevel_;
      }
      /**
       * <pre>
       * vip等级
       * </pre>
       *
       * <code>optional int32 vipLevel = 5 [default = -1];</code>
       */
      public Builder setVipLevel(int value) {
        bitField0_ |= 0x00000010;
        vipLevel_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * vip等级
       * </pre>
       *
       * <code>optional int32 vipLevel = 5 [default = -1];</code>
       */
      public Builder clearVipLevel() {
        bitField0_ = (bitField0_ & ~0x00000010);
        vipLevel_ = -1;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:com.su.proto.PlayerDataPro)
    }

    // @@protoc_insertion_point(class_scope:com.su.proto.PlayerDataPro)
    private static final com.su.proto.PlayerProto.PlayerDataPro DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new com.su.proto.PlayerProto.PlayerDataPro();
    }

    public static com.su.proto.PlayerProto.PlayerDataPro getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    @java.lang.Deprecated public static final com.google.protobuf.Parser<PlayerDataPro>
        PARSER = new com.google.protobuf.AbstractParser<PlayerDataPro>() {
      public PlayerDataPro parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new PlayerDataPro(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<PlayerDataPro> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<PlayerDataPro> getParserForType() {
      return PARSER;
    }

    public com.su.proto.PlayerProto.PlayerDataPro getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_su_proto_PlayerDataPro_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_su_proto_PlayerDataPro_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\022proto/Player.proto\022\014com.su.proto\"g\n\rPl" +
      "ayerDataPro\022\016\n\002id\030\001 \001(\003:\002-1\022\014\n\004name\030\002 \001(" +
      "\t\022\017\n\007picture\030\003 \001(\t\022\021\n\005level\030\004 \001(\005:\002-1\022\024\n" +
      "\010vipLevel\030\005 \001(\005:\002-1B\rB\013PlayerProto"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_com_su_proto_PlayerDataPro_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_su_proto_PlayerDataPro_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_su_proto_PlayerDataPro_descriptor,
        new java.lang.String[] { "Id", "Name", "Picture", "Level", "VipLevel", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
