#[derive(Debug, PartialEq, Clone)]
pub enum Node<'a> {
    NBoolean { value: bool },
    NByte { value: i8 },
    NShort { value: i16 },
    NInt { value: i32 },
    NLong { value: i64 },
    NFloat { value: f32 },
    NDouble { value: f64 },
    NNull,
    NClass {
        methods: Vec<Node<'a>>,
        fields: Vec<Node<'a>>
    },
    NMethodDeclaration {
        name: &'a str,
        ret: &'a str,
        args: Vec<&'a str>,
        body: Vec<Node<'a>>
    },
    NFieldDeclaration {
        name: &'a str,
        value_type: &'a str
    },
    NReference { identifier: &'a str },
    NAssignment { dest: &'a str, v: Box<Node<'a>> },
    NStaticReference { field: &'a str },
    NBoundReference { obj: Box<Node<'a>>, field: &'a str },
    NBoundAssignment { obj: Box<Node<'a>>, dest: &'a str, v: Box<Node<'a>> },
    NStaticCall { method: &'a str, args: Vec<Node<'a>> },
    NCall { obj: Box<Node<'a>>, method: &'a str, args: Vec<Node<'a>> },
    NNew { class: &'a str },
    NIAdd { left: Box<Node<'a>>, right: Box<Node<'a>> },
    NIMul { left: Box<Node<'a>>, right: Box<Node<'a>> },
    NLCmp { left: Box<Node<'a>>, right: Box<Node<'a>> },
    NReturn,
    NAReturn { obj: Box<Node<'a>> },
    NIReturn { obj: Box<Node<'a>> },
    NAThrow { v: Box<Node<'a>> },
    
    NOther { str: &'a str },

    Placeholder
}