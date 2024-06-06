
# Protobuf Code Generator

## Protobuf Code Generation Using Velocity

![](res/generate.gif)
![setting panel](res/setting.png)

## how to use:
### 1. Prepare a Protobuf File and Write a Message:
**Entity.proto:**

```protobuf
message Person {
    required int32 id = 1; // person id
    required string name = 2; // person name
}
```

### 2.Design a [Velocity](https://velocity.apache.org/engine/) Template File: 
**table.lua**


```lua
--[[
@Author: $author
@Date: $date
@Description: TODO:
]]
## Define a macro to format output
#macro(displayField $field)
    $field.name = $field.defaultValue,  -- pb_type: $field.type, pb_number: $field.number, pb_comment: $field.comment
#end
## Use the foreach directive to iterate over the field list
table $packageName.$messageName = {
#foreach( $field in $message.fields)
    #displayField($field)
#end
}
```
### 3.Click the Line Marker to Generate Code:

![generate](res/generate.gif)

**Generated Result**:

**Person.lua:**
The generated file has an extension consistent with the template file's extension, and the file name matches the message name.
```lua
--[[
@Author: Unknown
@Date: 2024-05-23
@Description: TODO:
]]
table Entity.Person = {
    id = 0,  -- pb_type: int32, pb_number: 1, pb_comment: person id
    name = "",  -- pb_type: string, pb_number: 2, pb_comment: person name
}
```

### If the File Already Exists, It Performs a Comparison Operation:
![compare](res/compare.gif)

## Other

### Enum
```protobuf
enum course{
  math = 0;//math comment
  english = 1;// english comment
}
```

```velocity
#macro(displayEnum $enum)
$enum.name = {
#foreach($enumValue in $enum.values)
    $enumValue.name = $enumValue.value --$enumValue.comment
#end
#if($enum)
#displayEnum($enum)
#end
```

```lua
course = {
    math = 0 --math comment
    english = 1 -- english comment
}
```

### Nested Message,MapField,Nested Enum
```protobuf3
message person{
  enum sex{
    male = 0;//male comment
    female = 1;//female comment
  }
  string name = 1;//name comment
  int32 age = 2;//age comment
  repeated string tags = 3;
  map<int32,string> course = 4;
  message cloth{
    int32 color =1;
  }
  repeated cloth clothes = 5;
}
```

```velocity
--[[
@Author: $author
@Date: $date
@Description: TODO:
]]

#macro(displayField $field)
#if ($field.isRepeated == false)
    $field.name = $field.defaultValue,  -- pb_type: $field.type, pb_number: $field.number, pb_comment: $field.comment
#else
    $field.name = {}
#end
#end
#macro(displayMapField $mapField)
    $mapField.name = {} ,  --keyType:$mapField.keyType,valueType:$mapField.valueType
#end
#macro(displayMessage $message)
$message.name = {
#foreach( $subMessage in $message.subMessages)
    #displayMessage($subMessage)
#end
#foreach( $enum in $message.enums)
    #displayEnum($enum)
#end
#foreach( $field in $message.fields)
    #displayField($field)
#end
#foreach( $mapField in $message.mapFields)
    #displayMapField($mapField)
#end
}
#end
#macro(displayEnum $enum)
$enum.name = {
#foreach($enumValue in $enum.values)
    $enumValue.name = $enumValue.value --$enumValue.comment
#end
}
#end
#if( $message )
#displayMessage($message)
#end
#if($enum)
#displayEnum($enum)
#end
```

```lua
--[[
@Author: Unknown
@Date: 2024-06-07
@Description: TODO:
]]

person = {
cloth = {
    color = 0,  -- pb_type: int32, pb_number: 1, pb_comment: 
}
sex = {
    male = 0 --male comment
    female = 1 --female comment
}
    name = "",  -- pb_type: string, pb_number: 1, pb_comment: name comment
    age = 0,  -- pb_type: int32, pb_number: 2, pb_comment: age comment
    tags = {}
    clothes = {}
    course = {} ,  --keyType:int32,valueType:string
}
```

## Dependencies
- **Protocol Buffers IntelliJ IDEs Plugin**
    - **Plugin link**: [Protocol Buffers - IntelliJ IDEs Plugin](https://plugins.jetbrains.com/plugin/14004-protocol-buffers)
    - **Copyright**: © 2000-2024 JetBrains s.r.o.
