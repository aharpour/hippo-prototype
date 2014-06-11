<#escape x as x?xml>
<#if content.quote??>
	<blockquote class="quoted">
		<div class="content">
			<span class="top"></span>
			<span class="bottom"></span>
			${content.quote }
		</div>
		<#if content.author??>
			<div>
				<span class="source">- ${content.author}</span>
			</div>
		</#if>
	</blockquote>
</#if>
</#escape>

