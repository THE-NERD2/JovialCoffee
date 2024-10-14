#[derive(Debug, PartialEq, Clone)]
pub enum Node {
    NBoolean { value: bool },
    NByte { value: i8 },
    NShort { value: i16 },
    NInt { value: i32 },
    NLong { value: i64 },
    NFloat { value: f32 },
    NDouble { value: f64 },
    NNull,
    NClass {
        name: String,
        methods: Vec<Node>,
        fields: Vec<Node>
    },
    NMethodDeclaration {
        name: String,
        ret: String,
        args: Vec<String>,
        body: Vec<Node>
    },
    NFieldDeclaration {
        name: String,
        value_type: String
    },
    NReference { identifier: String },
    NAssignment { dest: String, v: Box<Node> },
    NStaticReference { field: String },
    NBoundReference { obj: Box<Node>, field: String },
    NBoundAssignment { obj: Box<Node>, dest: String, v: Box<Node> },
    NStaticCall { method: String, args: Vec<Node> },
    NCall { obj: Box<Node>, method: String, args: Vec<Node> },
    NNew { class: String },
    NIAdd { left: Box<Node>, right: Box<Node> },
    NIMul { left: Box<Node>, right: Box<Node> },
    NLCmp { left: Box<Node>, right: Box<Node> },
    NReturn,
    NAReturn { obj: Box<Node> },
    NIReturn { obj: Box<Node> },
    NAThrow { v: Box<Node> },
    
    NOther { str: String },

    Placeholder
}