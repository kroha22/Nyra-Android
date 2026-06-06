package com.nyra.app.android.core.ui_state.composition

import com.nyra.app.android.core.content.repository.NyraContentRepository
import com.nyra.app.android.core.profile.model.NyraUserProfile
import com.nyra.app.android.core.ui_state.model.NyraTextTone
import javax.inject.Inject

class HomePromptResolver @Inject constructor(
    private val repository: NyraContentRepository
) {

    fun resolve(
        profile: NyraUserProfile,
        textTone: NyraTextTone
    ): HomePromptSet {
        val tags = buildList {
            addAll(profile.reflectionTags.take(TAG_LIMIT))
            addAll(toneTags[textTone].orEmpty())
            addAll(profile.activeArchetypes.map { it.archetypeId })
        }.distinct()
        val prompts = repository.getPromptsByTags(tags)

        return HomePromptSet(
            topPrompt = prompts.firstOrNull()?.prompt?.en,
            eveningReflectionPrompt = prompts.firstOrNull {
                "evening" in it.timeBias || "night" in it.timeBias
            }?.prompt?.en,
            checkInQuestion = prompts.firstOrNull {
                "emotional_processing" in it.reflectionTags ||
                    "inner_world" in it.lifeAreas ||
                    "check_in" in it.tags
            }?.prompt?.en
        )
    }

    private companion object {
        const val TAG_LIMIT = 12
        val toneTags = mapOf(
            NyraTextTone.CALM to listOf("stability", "grounding"),
            NyraTextTone.REFLECTIVE to listOf("inner_world", "meaning"),
            NyraTextTone.SUPPORTIVE to listOf("healing", "rest"),
            NyraTextTone.EXPRESSIVE to listOf("self_expression", "relationships"),
            NyraTextTone.ANALYTICAL to listOf("identity", "patterns"),
            NyraTextTone.EXPLORATORY to listOf("exploration", "change"),
            NyraTextTone.INTIMATE to listOf("trust", "vulnerability")
        )
    }
}
