package models

import com.xemantic.ai.tool.schema.StringFormat
import com.xemantic.ai.tool.schema.meta.Description
import com.xemantic.ai.tool.schema.meta.Format
import com.xemantic.ai.tool.schema.meta.MaxLength
import com.xemantic.ai.tool.schema.meta.MinLength
import com.xemantic.ai.tool.schema.meta.Pattern
import com.xemantic.ai.tool.schema.meta.Title
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("address")
@Title("The full address")
@Description("An address of a person or an organization")
data class Address(
    val street: String,
    val city: String,
    @Description("A postal code not limited to particular country")
    @MinLength(3)
    @MaxLength(10)
    val postalCode: String,
    @Pattern("[a-z]{2}")
    val countryCode: String,
    @Format(StringFormat.EMAIL)
    val email: String? = null,
)
