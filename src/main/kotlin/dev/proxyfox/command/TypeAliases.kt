package dev.proxyfox.command

import dev.proxyfox.command.node.CommandNode

public typealias Executor<T> = suspend CommandContext<T>.() -> Boolean

public typealias NodeActionParam<T, C, V> = suspend CommandNode<T, C>.(ParamGetter<T, V>) -> Unit

public typealias NodeAction<T, C> = suspend CommandNode<T, C>.() -> Unit

public typealias ParamGetter<T, V> = suspend CommandContext<T>.() -> V
