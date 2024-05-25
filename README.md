
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

## Dependencies
- **Protocol Buffers IntelliJ IDEs Plugin**
    - **Plugin link**: [Protocol Buffers - IntelliJ IDEs Plugin](https://plugins.jetbrains.com/plugin/14004-protocol-buffers)
    - **Copyright**: © 2000-2024 JetBrains s.r.o.
