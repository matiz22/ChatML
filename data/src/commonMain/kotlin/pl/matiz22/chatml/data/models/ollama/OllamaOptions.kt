package pl.matiz22.chatml.data.models.ollama

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OllamaOptions(
    @SerialName("frequency_penalty")
    val frequencyPenalty: Double? = null,
    @SerialName("low_vram")
    val lowVram: Boolean? = null,
    @SerialName("main_gpu")
    val mainGpu: Int? = null,
    @SerialName("min_p")
    val minP: Double? = null,
    @SerialName("mirostat")
    val mirostat: Int? = null,
    @SerialName("mirostat_eta")
    val mirostatEta: Double? = null,
    @SerialName("mirostat_tau")
    val mirostatTau: Double? = null,
    @SerialName("num_batch")
    val numBatch: Int? = null,
    @SerialName("num_ctx")
    val numCtx: Int? = null,
    @SerialName("num_gpu")
    val numGpu: Int? = null,
    @SerialName("num_keep")
    val numKeep: Int? = null,
    @SerialName("num_predict")
    val numPredict: Int? = null,
    @SerialName("num_thread")
    val numThread: Int? = null,
    @SerialName("numa")
    val numa: Boolean? = null,
    @SerialName("penalize_newline")
    val penalizeNewline: Boolean? = null,
    @SerialName("presence_penalty")
    val presencePenalty: Double? = null,
    @SerialName("repeat_last_n")
    val repeatLastN: Int? = null,
    @SerialName("repeat_penalty")
    val repeatPenalty: Double? = null,
    @SerialName("seed")
    val seed: Int? = null,
    @SerialName("stop")
    val stop: List<String>? = null,
    @SerialName("temperature")
    val temperature: Double? = null,
    @SerialName("tfs_z")
    val tfsZ: Double? = null,
    @SerialName("top_k")
    val topK: Int? = null,
    @SerialName("top_p")
    val topP: Double? = null,
    @SerialName("typical_p")
    val typicalP: Double? = null,
    @SerialName("use_mlock")
    val useMlock: Boolean? = null,
    @SerialName("use_mmap")
    val useMmap: Boolean? = null,
    @SerialName("vocab_only")
    val vocabOnly: Boolean? = null,
)
