#include <llvm/IR/Constants.h>
#include <vector>
#include <string>

using namespace std;
using namespace llvm;

class Node {
public:
    Node() {}
    virtual Value* codegen();
};

class NBoolean: public Node {
public:
    bool value;
    NBoolean(bool value): value(value) {}
    virtual Value* codegen();
};

class NByte: public Node {
public:
    int8_t value;
    NByte(int8_t value): value(value) {}
    virtual Value* codegen();
};

class NShort: public Node {
public:
    short value;
    NShort(short value): value(value) {}
    virtual Value* codegen();
};

class NInt: public Node {
public:
    int value;
    NInt(int value): value(value) {}
    virtual Value* codegen();
};

class NLong: public Node {
public:
    long value;
    NLong(long value): value(value) {}
    virtual Value* codegen();
};

class NFloat: public Node {
public:
    float value;
    NFloat(float value): value(value) {}
    virtual Value* codegen();
};

class NDouble: public Node {
public:
    double value;
    NDouble(double value): value(value) {}
    virtual Value* codegen();
};

class NNull: public Node {
public:
    NNull() {}
    virtual Value* codegen();
};

class NClass: public Node {
public:
    string name;
    vector<NFieldDeclaration> fields;
    vector<NMethodDeclaration> methods;
    NClass(string name): name(name) {}
    virtual Value* codegen();
};

class NFieldDeclaration: public Node {
public:
    string name;
    string type;
    NFieldDeclaration(string name, string type): name(name), type(type) {}
    virtual Value* codegen();
};

class NMethodDeclaration: public Node {
public:
    string name;
    string ret;
    vector<string> args;
    vector<Node> body;
    NMethodDeclaration(string name, string ret, string args[]): name(name), ret(ret) {}
    virtual Value* codegen();
};

class NReference: public Node {
public:
    string identifier;
    NReference(string identifier): identifier(identifier) {}
    virtual Value* codegen();
};

class NAssignment: public Node {
public:
    string dest;
    Node value;
    NAssignment(string dest, Node value): dest(dest), value(value) {}
    virtual Value* codegen();
};

class NStaticReference: public Node {
public:
    string field;
    NStaticReference(string field): field(field) {}
    virtual Value* codegen();
};

class NStaticAssignment: public Node {
public:
    string field;
    Node value;
    NStaticAssignment(string field, Node value): field(field), value(value) {}
    virtual Value* codegen();
};

class NBoundReference: public Node {
public:
    Node obj;
    string field;
    NBoundReference(Node obj, string field): obj(obj), field(field) {}
    virtual Value* codegen();
};

class NBoundAssignment: public Node {
public:
    Node obj;
    string field;
    Node value;
    NBoundAssignment(Node obj, string field, Node value): obj(obj), field(field), value(value) {}
    virtual Value* codegen();
};

class NStaticCall: public Node {
public:
    string method;
    vector<Node> args;
    NStaticCall(string method): method(method) {}
    virtual Value* codegen();
};

class NCall: public Node {
public:
    Node obj;
    string method;
    vector<Node> args;
    NCall(Node obj, string method): obj(obj), method(method) {}
    virtual Value* codegen();
};

class NNew: public Node {
public:
    string clazz;
    NNew(string clazz): clazz(clazz) {}
    virtual Value* codegen();
};

class NBinOp: public Node {
public:
    string type;
    string op;
    Node left;
    Node right;
    NBinOp(string type, string op, Node left, Node right): type(type), op(op), left(left), right(right) {}
    virtual Value* codegen();
};

class NNewArray: public Node {
public:
    string type;
    Node length;
    NNewArray(string type, Node length): type(type), length(length) {}
    virtual Value* codegen();
};

class NArrayReference: public Node {
public:
    Node array;
    Node index;
    NArrayReference(Node array, Node index): array(array), index(index) {}
    virtual Value* codegen();
};

class NArrayAssignment: public Node {
public:
    Node array;
    Node index;
    Node value;
    NArrayAssignment(Node array, Node index, Node value): array(array), index(index), value(value) {}
    virtual Value* codegen();
};

class NArrayLength: public Node {
public:
    Node array;
    NArrayLength(Node array): array(array) {}
    virtual Value* codegen();
};

class NReturn: public Node {
public:
    NReturn() {}
    virtual Value* codegen();
};

class NValueReturn: public Node {
public:
    string type;
    Node value;
    NValueReturn(string type, Node value): type(type), value(value) {}
    virtual Value* codegen();
};

class NAThrow: public Node {
public:
    Node value;
    NAThrow(Node value): value(value) {}
    virtual Value* codegen();
};

class NNot: public Node {
public:
    Node condition;
    NNot(Node condition): condition(condition) {}
    virtual Value* codegen();
};

class NIf: public Node {
public:
    Node condition;
    vector<Node> ifBranch;
    vector<Node> elseBranch;
    NIf(Node condition): condition(condition) {}
    virtual Value* codegen();
};

class NLoop: public Node {
public:
    Node condition;
    vector<Node> body;
    NLoop(Node condition): condition(condition) {}
    virtual Value* codegen();
};