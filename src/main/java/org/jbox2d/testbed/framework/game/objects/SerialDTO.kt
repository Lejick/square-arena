package org.jbox2d.testbed.framework.game.objects
import org.jbox2d.common.Vec2

class SerialDTO(var step: Long, var id: Int, var className: String,
                var linearVelocity: Vec2, var angularVelocity: Float, var position: Vec2, var levelId: Long)