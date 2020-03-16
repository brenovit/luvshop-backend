"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Rectangle_1 = require("./Rectangle");
var Circle_1 = require("./Circle");
var myCircle = new Circle_1.Circle(0, 0, 5);
console.log(myCircle.getInfo());
var myRectangle = new Rectangle_1.Rectangle(10, 10, 20, 30);
console.log(myRectangle.getInfo());
