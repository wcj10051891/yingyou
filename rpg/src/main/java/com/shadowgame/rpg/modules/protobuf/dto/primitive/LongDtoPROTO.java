// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: com/shadowgame/rpg/modules/protocol/dto/primitive/LongDto_PROTO.proto

package com.shadowgame.rpg.modules.protobuf.dto.primitive;

public final class LongDtoPROTO {
  private LongDtoPROTO() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface LongDtoOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
  }
  /**
   * Protobuf type {@code com.shadowgame.rpg.modules.protocol.dto.primitive.LongDto}
   *
   * <pre>
   **
   * 
   * @author wcj10051891@gmail.com
   *字段列表:
   * </pre>
   */
  public static final class LongDto extends
      com.google.protobuf.GeneratedMessage
      implements LongDtoOrBuilder {
    // Use LongDto.newBuilder() to construct.
    private LongDto(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private LongDto(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final LongDto defaultInstance;
    public static LongDto getDefaultInstance() {
      return defaultInstance;
    }

    public LongDto getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private LongDto(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.internal_static_com_shadowgame_rpg_modules_protocol_dto_primitive_LongDto_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.internal_static_com_shadowgame_rpg_modules_protocol_dto_primitive_LongDto_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto.class, com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto.Builder.class);
    }

    public static com.google.protobuf.Parser<LongDto> PARSER =
        new com.google.protobuf.AbstractParser<LongDto>() {
      public LongDto parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new LongDto(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<LongDto> getParserForType() {
      return PARSER;
    }

    private void initFields() {
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code com.shadowgame.rpg.modules.protocol.dto.primitive.LongDto}
     *
     * <pre>
     **
     * 
     * @author wcj10051891@gmail.com
     *字段列表:
     * </pre>
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDtoOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.internal_static_com_shadowgame_rpg_modules_protocol_dto_primitive_LongDto_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.internal_static_com_shadowgame_rpg_modules_protocol_dto_primitive_LongDto_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto.class, com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto.Builder.class);
      }

      // Construct using com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.internal_static_com_shadowgame_rpg_modules_protocol_dto_primitive_LongDto_descriptor;
      }

      public com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto getDefaultInstanceForType() {
        return com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto.getDefaultInstance();
      }

      public com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto build() {
        com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto buildPartial() {
        com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto result = new com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto(this);
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto) {
          return mergeFrom((com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto other) {
        if (other == com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto.getDefaultInstance()) return this;
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.shadowgame.rpg.modules.protobuf.dto.primitive.LongDtoPROTO.LongDto) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      // @@protoc_insertion_point(builder_scope:com.shadowgame.rpg.modules.protocol.dto.primitive.LongDto)
    }

    static {
      defaultInstance = new LongDto(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:com.shadowgame.rpg.modules.protocol.dto.primitive.LongDto)
  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_com_shadowgame_rpg_modules_protocol_dto_primitive_LongDto_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_com_shadowgame_rpg_modules_protocol_dto_primitive_LongDto_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\nEcom/shadowgame/rpg/modules/protocol/dt" +
      "o/primitive/LongDto_PROTO.proto\0221com.sha" +
      "dowgame.rpg.modules.protocol.dto.primiti" +
      "ve\"\t\n\007LongDtoB3\n1com.shadowgame.rpg.modu" +
      "les.protobuf.dto.primitive"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_com_shadowgame_rpg_modules_protocol_dto_primitive_LongDto_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_com_shadowgame_rpg_modules_protocol_dto_primitive_LongDto_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_com_shadowgame_rpg_modules_protocol_dto_primitive_LongDto_descriptor,
              new java.lang.String[] { });
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}